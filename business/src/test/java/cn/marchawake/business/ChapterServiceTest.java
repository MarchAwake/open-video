package cn.marchawake.business;

import cn.marchawake.server.dto.ChapterDto;
import cn.marchawake.server.service.ChapterService;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Chapter服务测试
 *
 * @author March
 * @date 2020/7/6
 */
@SpringBootTest
public class ChapterServiceTest {

    @Autowired
    private ChapterService service;

    @Test
    public void createChapterTest() {
        ChapterDto chapterDto = new ChapterDto();
        chapterDto.setCourseId("xcbzg2");
        chapterDto.setName("Java从入门到精");
        service.save(chapterDto);
        System.out.println(JSON.toJSONString(chapterDto));
    }

    @Test
    public void selectChapterTest() {

    }


}
