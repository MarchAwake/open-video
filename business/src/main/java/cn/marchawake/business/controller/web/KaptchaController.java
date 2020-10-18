package cn.marchawake.business.controller.web;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/web/kaptcha")
@Api(tags = "web-验证码操作的相关接口")
public class KaptchaController {

    /** 业务名称 */
    public static final String BUSINESS_NAME = "获取验证码";

    @Qualifier("getWebKaptcha")
    @Autowired
    DefaultKaptcha defaultKaptcha;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public KaptchaController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        // 设置序列化标准
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    @GetMapping("/image-code/{imageCodeToken}")
    public void imageCode(@PathVariable(value = "imageCodeToken") String imageCodeToken, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception{
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生成验证码字符串
            String createText = defaultKaptcha.createText();
            // 设置序列化标准
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.setKeySerializer(new StringRedisSerializer());

            // 将生成的验证码放入redis缓存中，后续验证的时候用到
            redisTemplate.opsForValue().set(imageCodeToken, createText, 300, TimeUnit.SECONDS);

            // 使用验证码字符串生成验证码图片
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
