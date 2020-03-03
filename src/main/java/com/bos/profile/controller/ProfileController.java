package com.bos.profile.controller;

import bca.bit.proj.library.base.ResultEntity;
import com.bos.profile.model.ProfileDetail;
import com.bos.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/bos", produces = "application/json")
@CrossOrigin(origins = {"*"})
public class ProfileController {
    @Autowired
    ProfileService g_profileService;

    @GetMapping(value = "/profile/{id_seller}")
    public ResultEntity getProfile(@PathVariable("id_seller") int id_seller){
        return g_profileService.getProfile(id_seller);
    }

    @PutMapping(value = "/profile", consumes = "application/json")
    public ResultEntity updateProfile(@RequestBody ProfileDetail p_profileDetail){
        return g_profileService.updateProfile(p_profileDetail);
    }

}
