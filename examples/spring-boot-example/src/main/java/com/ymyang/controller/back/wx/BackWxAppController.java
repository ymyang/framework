package com.ymyang.controller.back.wx;

import com.ymyang.service.wx.WxAppService;
import com.ymyang.param.wx.WxAppQueryParam;
import com.ymyang.param.wx.WxAppCreateParam;
import com.ymyang.param.wx.WxAppUpdateParam;
import com.ymyang.entity.wx.WxAppEntity;
import com.ymyang.dto.wx.WxAppDto;

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
 * 微信公众号
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@Api(value = "wx.WxAppController", tags = {"微信公众号"})
@RestController("wx.BackWxAppController")
@RequestMapping("/api/back/wx/wxapp")
public class BackWxAppController {

	@Autowired
	private WxAppService wxAppService;

    /**
     * 列表
     */
    @ApiOperation(value = "微信公众号列表", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxapp:list")
    @GetMapping("/list")
    public ResponseEntity<IPage<WxAppDto>> list(WxAppQueryParam param) {

        IPage<WxAppEntity> list = wxAppService.page(param);

        // 多一步转换操作，因不推荐直接返回Entity
        IPage<WxAppDto> convert = list.convert((item)->{
            WxAppDto wxAppDto = new WxAppDto();
            wxAppDto.copyPropertiesFrom(item, false);
            return wxAppDto;
        });

        return ResponseEntity.success(convert);
    }

    /**
     * 详情
     */
    @ApiOperation(value = "微信公众号详情", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxapp:detail")
    @GetMapping("/{id}")
    public ResponseEntity<WxAppDto> detail(@PathVariable("id") Long id) {

        WxAppEntity wxApp = wxAppService.getById(id);

        // 多一步转换操作，因不推荐直接返回Entity
        WxAppDto wxAppDto = new WxAppDto();
        wxAppDto.copyPropertiesFrom(wxApp, false);

        return ResponseEntity.success(wxAppDto);
    }

    /**
     * 添加
     */
    @ApiOperation(value = "微信公众号新增", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxapp:create")
    @PostMapping("")
    public ResponseEntity<WxAppDto> create(@Validated @RequestBody WxAppCreateParam param) {

		WxAppEntity entity = wxAppService.create(param);
		if (entity == null) {
			return ResponseEntity.error("添加失败");
		}
        // 多一步转换操作，因不推荐直接返回Entity
        WxAppDto dto = new WxAppDto();
        dto.copyPropertiesFrom(entity, false);

        return ResponseEntity.success(dto, "添加成功");

    }

    /**
     * 修改
     */
    @ApiOperation(value = "微信公众号修改", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxapp:update")
    @PutMapping("")
    public ResponseEntity<WxAppDto> update(@Validated @RequestBody WxAppUpdateParam param) {
		WxAppEntity entity = wxAppService.update(param);
		if (entity == null) {
			return ResponseEntity.error("修改失败");
		}

        WxAppDto dto = new WxAppDto();
        dto.copyPropertiesFrom(entity, false);

        return ResponseEntity.success(dto, "修改成功");

    }

    /**
     * 删除
     */
    @ApiOperation(value = "微信公众号删除", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxapp:delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {

        wxAppService.removeById(id);

        return ResponseEntity.success("删除成功");
    }

}
