package com.sg.shopping.controller;

import com.sg.shopping.common.utils.CookieUtils;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.vo.UserInfoVO;
import com.sg.shopping.resource.FileResource;
import com.sg.shopping.service.FdfsService;
import com.sg.shopping.service.center.CenterUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("fdfs")
public class CenterUserController extends BaseController {

    @Autowired
    private FileResource fileResource;

    @Autowired
    private FdfsService fdfsService;

    @Autowired
    private CenterUserService centerUserService;

    @PostMapping("uploadFace")
    public JsonResult uploadFace(
            String userId,
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
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

        String path = fdfsService.upload(file, suffix);
        System.out.println("uploaded file to " + path);

        if (StringUtils.isNotBlank(path)) {

            String finalUserFaceUrl = fileResource.getHost() + path;
            UserInfo userInfo = centerUserService.updateUserFace(userId, finalUserFaceUrl);
            UserInfoVO userInfoVO = convertToUserInfoVo(userInfo);

            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userInfoVO), true);
        } else {
            return JsonResult.errorMsg("upload file failed");
        }
        return JsonResult.ok();
    }

}
