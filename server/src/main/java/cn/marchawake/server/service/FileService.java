package cn.marchawake.server.service;

import cn.marchawake.server.dto.FileDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.File;
import cn.marchawake.server.entity.FileExample;
import cn.marchawake.server.mapper.FileMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <h1>File 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class FileService {

    /** 文件持久化接口 */
    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public ResponseDto list(PageDto<FileDto> pageDto) {

        // 分页查询&升序
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize());
        FileExample example = new FileExample();
        example.setOrderByClause("updated_at asc");
        List<File> fileList = fileMapper.selectByExample(example);

        pageDto.setTotal(new PageInfo<>(fileList).getTotal());
        pageDto.setList(CopyUtil.copyList(fileList, FileDto.class));

        return ResponseDto.success(pageDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto save(FileDto fileDto) {

        File file = CopyUtil.copy(fileDto, File.class);
        File fileDb = selectByKey(fileDto.getKey());
        if (fileDb == null) {
            return this.insert(file);
        }else {
            fileDb.setShardIndex(fileDto.getShardIndex());
            return this.update(fileDb);
        }
    }

    private ResponseDto update(File file) {

        file.setUpdatedAt(new Date());
        return fileMapper.updateByPrimaryKey(file) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    private ResponseDto insert(File file) {

        file.setId(UuidUtil.getShortUuid());
        Date now = new Date();
        file.setCreatedAt(now);
        file.setUpdatedAt(now);
        return fileMapper.insert(file) == 1 ?
                ResponseDto.success() : ResponseDto.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto delete(String id) {

        return fileMapper.deleteByPrimaryKey(id) == 1 ?
            ResponseDto.success() : ResponseDto.error();
    }

    /**
     * 根据文件标识查询数据库记录
     */
    public FileDto findByKey(String key) {
        return CopyUtil.copy(selectByKey(key), FileDto.class);
    }

    /**
     * 根据文件标识查询数据库记录
     */
    public FileDto findByVod(String vod) {
        return CopyUtil.copy(selectByVod(vod), FileDto.class);
    }

    public File selectByKey(String key) {

        FileExample example = new FileExample();
        example.createCriteria().andKeyEqualTo(key);
        List<File> fileList = fileMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(fileList)) {
            return null;
        } else {
            return fileList.get(0);
        }
    }

    public File selectByVod(String vod) {

        FileExample example = new FileExample();
        example.createCriteria().andVodEqualTo(vod);
        List<File> fileList = fileMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(fileList)) {
            return null;
        } else {
            return fileList.get(0);
        }
    }

}
