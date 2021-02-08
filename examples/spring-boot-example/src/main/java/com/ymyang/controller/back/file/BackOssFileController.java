package com.ymyang.controller.back.file;

import com.ymyang.service.file.OssFileService;
import com.ymyang.param.file.OssFileQueryParam;
import com.ymyang.param.file.OssFileCreateParam;
import com.ymyang.param.file.OssFileUpdateParam;
import com.ymyang.entity.file.OssFileEntity;
import com.ymyang.dto.file.OssFileDto;

import com.ymyang.framework.beans.ResponseEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.Arrays;


/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:41:15
 */
@Api(value = "file.OssFileController", tags = {""})
@RestController("file.BackOssFileController")
@RequestMapping("/api/back/file/ossfile")
public class BackOssFileController {

	@Autowired
	private OssFileService ossFileService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", notes = "作者：ymyang")
    @RequiresPermissions("file:ossfile:list")
    @GetMapping("/list")
    public ResponseEntity<IPage<OssFileDto>> list(OssFileQueryParam param) {

        IPage<OssFileEntity> list = ossFileService.page(param);

        // 多一步转换操作，因不推荐直接返回Entity
        IPage<OssFileDto> convert = list.convert((item)->{
            OssFileDto ossFileDto = new OssFileDto();
            ossFileDto.copyPropertiesFrom(item, false);
            return ossFileDto;
        });

        return ResponseEntity.success(convert);
    }

    /**
     * 详情
     */
    @ApiOperation(value = "详情", notes = "作者：ymyang")
    @RequiresPermissions("file:ossfile:detail")
    @GetMapping("/{id}")
    public ResponseEntity<OssFileDto> detail(@PathVariable("id") Long id) {

        OssFileEntity ossFile = ossFileService.getById(id);

        // 多一步转换操作，因不推荐直接返回Entity
        OssFileDto ossFileDto = new OssFileDto();
        ossFileDto.copyPropertiesFrom(ossFile, false);

        return ResponseEntity.success(ossFileDto);
    }

    /**
     * 添加
     */
    @ApiOperation(value = "新增", notes = "作者：ymyang")
    @RequiresPermissions("file:ossfile:create")
    @PostMapping("")
    public ResponseEntity<OssFileDto> create(@Validated @RequestBody OssFileCreateParam param) {

		OssFileEntity entity = ossFileService.create(param);
		if (entity == null) {
			return ResponseEntity.error("添加失败");
		}
        // 多一步转换操作，因不推荐直接返回Entity
        OssFileDto dto = new OssFileDto();
        dto.copyPropertiesFrom(entity, false);

        return ResponseEntity.success(dto, "添加成功");

    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改", notes = "作者：ymyang")
    @RequiresPermissions("file:ossfile:update")
    @PutMapping("")
    public ResponseEntity<OssFileDto> update(@Validated @RequestBody OssFileUpdateParam param) {
		OssFileEntity entity = ossFileService.update(param);
		if (entity == null) {
			return ResponseEntity.error("修改失败");
		}

        OssFileDto dto = new OssFileDto();
        dto.copyPropertiesFrom(entity, false);

        return ResponseEntity.success(dto, "修改成功");

    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除", notes = "作者：ymyang")
    @RequiresPermissions("file:ossfile:delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {

        ossFileService.removeById(id);

        return ResponseEntity.success("删除成功");
    }

}
