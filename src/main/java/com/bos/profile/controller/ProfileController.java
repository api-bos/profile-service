package com.bos.profile.controller;

import bca.bit.proj.library.base.ResultEntity;
import bca.bit.proj.library.enums.ErrorCode;
import com.bos.profile.model.ChangePassword;
import com.bos.profile.model.ProfileRequest;
import com.bos.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/bos", produces = "application/json")
public class ProfileController {
    @Autowired
    ProfileService g_profileService;

    @GetMapping(value = "/profile/{id_seller}")
    public ResultEntity getProfile(@PathVariable("id_seller") int id_seller){
        return g_profileService.getProfile(id_seller);
    }

    @PutMapping(value = "/profile", consumes = "application/json")
    public ResultEntity updateProfile(@RequestBody ProfileRequest p_profileRequest){
        return g_profileService.updateProfile(p_profileRequest);
    }

    @PostMapping(value = "/profile/pass", consumes = "application/json")
    public ResultEntity changePass(@RequestBody ChangePassword changePassword){
        try {
            System.out.println("Try cpass service");
            return g_profileService.changePass(changePassword);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ResultEntity(null, ErrorCode.BIT_999);
        }
    }
}
