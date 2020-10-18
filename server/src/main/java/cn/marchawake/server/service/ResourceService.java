package cn.marchawake.server.service;

import cn.marchawake.server.dto.ResourceDto;
import cn.marchawake.server.dto.PageDto;
import cn.marchawake.server.dto.ResponseDto;
import cn.marchawake.server.entity.Resource;
import cn.marchawake.server.entity.ResourceExample;
import cn.marchawake.server.mapper.ResourceMapper;
import cn.marchawake.server.utils.CopyUtil;
import cn.marchawake.server.utils.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Resource 表服务接口实现定义</h1>
 *
 * @author March
 * @date 2020/7/2
 */
@Service
@Slf4j
public class ResourceService {

    /** 资源表持久化接口 */
    private final ResourceMapper resourceMapper;

    @Autowired
    public ResourceService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }


    /**
     * 按约定将列表转成树，正序排列
     */
    public ResponseDto loadTree() {

        ResourceExample example = new ResourceExample();
        example.setOrderByClause("id asc");
        List<Resource> resourceList = resourceMapper.selectByExample(example);
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);

        for (int i = resourceDtoList.size() - 1; i >= 0; i--) {

            // 当前要移动的记录
            ResourceDto child = resourceDtoList.get(i);

            // 如果当前节点没有父节点，则不用往下了
            if (StringUtils.isEmpty(child.getParent())) {
                continue;
            }

            // 查找父节点
            for (int j = i - 1; j >= 0; j--) {

                ResourceDto parent = resourceDtoList.get(j);

                if (!child.getParent().equals(parent.getId())) {
                    continue;
                }

                if (CollectionUtils.isEmpty(parent.getChildren())) {
                    parent.setChildren(new LinkedList<>());
                }

                // 添加到最前面，否则会变成倒序，因为循环是从后往前循环的
                parent.getChildren().add(0, child);

                // 子节点找到父节点后，删除列表中的子节点
                resourceDtoList.remove(child);

            }

        }
        return ResponseDto.success(resourceDtoList);
    }


    /**
     * 保存资源树
     */
    @Transactional
    public ResponseDto saveJson(String json) {

        List<ResourceDto> jsonList = JSON.parseArray(json, ResourceDto.class);
        log.info("JSONList:{}",jsonList);
        if (CollectionUtils.isEmpty(jsonList)) {
            return ResponseDto.failure();
        }

        List<ResourceDto> list = new LinkedList<>();

        for (ResourceDto element: jsonList) {
            element.setParent("");
            add(list, element);
        }

        log.info("插入数据条数：{}",  list.size());

        resourceMapper.deleteByExample(null);

        for (ResourceDto resourceDto : list) {
            resourceMapper.insert(CopyUtil.copy(resourceDto, Resource.class));
        }

        return ResponseDto.success();
    }


    /**
     * 递归将树型结构的节点全部取出来，存放到list
     */
    private void add(List<ResourceDto> list, ResourceDto resourceDto) {

          list.add(resourceDto);

          if (!CollectionUtils.isEmpty(resourceDto.getChildren())) {
              for (ResourceDto element: resourceDto.getChildren()) {
                  element.setParent(resourceDto.getId());
                  add(list, element);
              }
          }

    }

}
