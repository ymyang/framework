package com.ymyang.controller.back.wx;

import com.ymyang.service.wx.WxMemberService;
import com.ymyang.param.wx.WxMemberQueryParam;
import com.ymyang.param.wx.WxMemberCreateParam;
import com.ymyang.param.wx.WxMemberUpdateParam;
import com.ymyang.entity.wx.WxMemberEntity;
import com.ymyang.dto.wx.WxMemberDto;

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
 * 微信用户信息
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@Api(value = "wx.WxMemberController", tags = {"微信用户信息"})
@RestController("wx.BackWxMemberController")
@RequestMapping("/api/back/wx/wxmember")
public class BackWxMemberController {

	@Autowired
	private WxMemberService wxMemberService;

    /**
     * 列表
     */
    @ApiOperation(value = "微信用户信息列表", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxmember:list")
    @GetMapping("/list")
    public ResponseEntity<IPage<WxMemberDto>> list(WxMemberQueryParam param) {

        IPage<WxMemberEntity> list = wxMemberService.page(param);

        // 多一步转换操作，因不推荐直接返回Entity
        IPage<WxMemberDto> convert = list.convert((item)->{
            WxMemberDto wxMemberDto = new WxMemberDto();
            wxMemberDto.copyPropertiesFrom(item, false);
            return wxMemberDto;
        });

        return ResponseEntity.success(convert);
    }

    /**
     * 详情
     */
    @ApiOperation(value = "微信用户信息详情", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxmember:detail")
    @GetMapping("/{id}")
    public ResponseEntity<WxMemberDto> detail(@PathVariable("id") Long id) {

        WxMemberEntity wxMember = wxMemberService.getById(id);

        // 多一步转换操作，因不推荐直接返回Entity
        WxMemberDto wxMemberDto = new WxMemberDto();
        wxMemberDto.copyPropertiesFrom(wxMember, false);

        return ResponseEntity.success(wxMemberDto);
    }

    /**
     * 添加
     */
    @ApiOperation(value = "微信用户信息新增", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxmember:create")
    @PostMapping("")
    public ResponseEntity<WxMemberDto> create(@Validated @RequestBody WxMemberCreateParam param) {

		WxMemberEntity entity = wxMemberService.create(param);
		if (entity == null) {
			return ResponseEntity.error("添加失败");
		}
        // 多一步转换操作，因不推荐直接返回Entity
        WxMemberDto dto = new WxMemberDto();
        dto.copyPropertiesFrom(entity, false);

        return ResponseEntity.success(dto, "添加成功");

    }

    /**
     * 修改
     */
    @ApiOperation(value = "微信用户信息修改", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxmember:update")
    @PutMapping("")
    public ResponseEntity<WxMemberDto> update(@Validated @RequestBody WxMemberUpdateParam param) {
		WxMemberEntity entity = wxMemberService.update(param);
		if (entity == null) {
			return ResponseEntity.error("修改失败");
		}

        WxMemberDto dto = new WxMemberDto();
        dto.copyPropertiesFrom(entity, false);

        return ResponseEntity.success(dto, "修改成功");

    }

    /**
     * 删除
     */
    @ApiOperation(value = "微信用户信息删除", notes = "作者：ymyang")
    @RequiresPermissions("wx:wxmember:delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {

        wxMemberService.removeById(id);

        return ResponseEntity.success("删除成功");
    }

}
