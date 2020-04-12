package com.sg.shopping.controller.center;

import com.sg.shopping.common.utils.CookieUtils;
import com.sg.shopping.common.utils.DateUtil;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.controller.BaseController;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.center.CenterUserBO;
import com.sg.shopping.resource.FileUpload;
import com.sg.shopping.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Center User info related APIs", tags = {"Center User info related APIs tag"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("update")
    @ApiOperation(value = "user info", notes = "get user info", httpMethod = "POST")
    public JsonResult userInfo(
            @ApiParam(name = "userId", value = "user id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO userBO,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return JsonResult.errorMap(getErrors(bindingResult));
        }
        UserInfo userInfo = centerUserService.updateUserInfo(userId, userBO);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userInfo), true);

        return JsonResult.ok(userInfo);
    }

    @PostMapping("uploadFace")
    @ApiOperation(value = "update user icon", notes = "update user icon", httpMethod = "POST")
    public JsonResult uploadFace(
            @ApiParam(name = "userId", value = "user id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "icon file", required = true)
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {

        FileOutputStream fos = null;
        InputStream fis = null;
        String fileUploadPath = "";
        try {
            String fileSpace = fileUpload.getImageUserFaceLocation();
            String uploadPathPrefix = File.separator + userId;

            if (file == null) {
                return JsonResult.errorMap("file cannot be null");
            }

            String iconFileName = file.getOriginalFilename();
            if (StringUtils.isBlank(iconFileName)) {
                return JsonResult.errorMsg("file name cannot be null");
            }

            String fileNameArr[] = iconFileName.split("\\.");
            String suffix = fileNameArr[fileNameArr.length - 1];

            if (!suffix.equalsIgnoreCase("png")
                    && !suffix.equalsIgnoreCase("jpg")
                    && !suffix.equalsIgnoreCase("jpeg")) {
                return JsonResult.errorMsg("File format incorrect.");
            }

            String newFileName = "face-" + userId + "." + suffix;
            String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
            fileUploadPath = uploadPathPrefix + File.separator + newFileName;

            File outFile = new File(finalFacePath);
            if (outFile.getParentFile() != null) {
                outFile.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(outFile);
            fis = file.getInputStream();
            IOUtils.copy(fis, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String finalUserFaceUrl = fileUpload.getImageServerUrl() + "/" + fileUploadPath + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        UserInfo userInfo = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userInfo), true);

        return JsonResult.ok(userInfo);
    }

    private UserInfo setNullProperty(UserInfo userInfo) {
        userInfo.setPassword(null);
        userInfo.setMobile(null);
        userInfo.setEmail(null);
        userInfo.setBirthday(null);
        userInfo.setCreatedTime(null);
        userInfo.setUpdatedTime(null);
        return userInfo;
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error: errors) {
            String errorField = error.getField();
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
        }
        return map;
    }
}
