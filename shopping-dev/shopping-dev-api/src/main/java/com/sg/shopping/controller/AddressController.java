package com.sg.shopping.controller;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.MobileEmailUtils;
import com.sg.shopping.pojo.bo.AddressBO;
import com.sg.shopping.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Address related APIs", tags = {"Address related APIs"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    @ApiOperation(value = "list", notes = "List all the address for a user", httpMethod = "POST")
    public Object list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }
        return JsonResult.ok(addressService.queryAll(userId));
    }

    @PostMapping("/add")
    @ApiOperation(value = "add", notes = "Add a new address for a user", httpMethod = "POST")
    public Object add(@RequestBody AddressBO addressBO) {
        JsonResult checkResult = checkAddress(addressBO);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        addressService.addNewUserAddress(addressBO);
        return JsonResult.ok();
    }

    @PostMapping("/update")
    @ApiOperation(value = "update", notes = "Update address for a user", httpMethod = "POST")
    public Object update(@RequestBody AddressBO addressBO) {
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return JsonResult.errorMsg("address id cannot be null");
        }

        JsonResult checkResult = checkAddress(addressBO);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        addressService.updateUserAddress(addressBO);
        return JsonResult.ok();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "delete", notes = "Delete address for a user", httpMethod = "POST")
    public Object delete(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("user id or address id cannot be null");
        }

        addressService.deleteUserAddress(userId, addressId);
        return JsonResult.ok();
    }

    @PostMapping("/setDefalut")
    @ApiOperation(value = "setDefault", notes = "Set address as default address for a user", httpMethod = "POST")
    public Object setDefault(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("user id or address id cannot be null");
        }

        addressService.updateUserAddressToBeDefaultAddress(userId, addressId);
        return JsonResult.ok();
    }

    private JsonResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JsonResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JsonResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JsonResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return JsonResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JsonResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return JsonResult.errorMsg("收货地址信息不能为空");
        }

        return JsonResult.ok();
    }
}
