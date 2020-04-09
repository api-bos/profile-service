package com.bos.profile.service;

import bca.bit.proj.library.base.ResultEntity;
import bca.bit.proj.library.enums.ErrorCode;
import com.bos.profile.model.ChangePassword;
import com.bos.profile.model.Profile;
import com.bos.profile.model.ProfileResponse;
import com.bos.profile.model.ProfileRequest;
import com.bos.profile.repository.KotaKabupatenRepository;
import com.bos.profile.repository.ProfileRepository;
import com.bos.profile.repository.SelectedCourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository g_profileRepository;
    @Autowired
    SelectedCourierRepository g_selectedCourierRepository;
    @Autowired
    KotaKabupatenRepository g_kotaKabupatenRepository;

    private String saveImage(ProfileRequest p_profileRequest){
        String tmp_fileName = p_profileRequest.getId_seller() + ".jpg";
        String tmp_fullPath = "\\NASBOS\\" + tmp_fileName;

        try(FileOutputStream tmp_imageOutFile = new FileOutputStream(tmp_fullPath)){
            byte[] tmp_imageByteArray = Base64.getDecoder().decode(p_profileRequest.getBase64StringImage());
            tmp_imageOutFile.write(tmp_imageByteArray);

            return tmp_fullPath;

        }catch (Exception e){
            System.out.println(e.toString());
            return "failed";
        }
    }

    public ResultEntity getProfile(int id_seller){
        ResultEntity l_output;

        try{
            Profile tmp_profile = g_profileRepository.getOne(id_seller);
            ProfileResponse tmp_profileResponse = new ProfileResponse();
            tmp_profileResponse.setId_seller(id_seller);
            tmp_profileResponse.setUsername(tmp_profile.getUsername());
            tmp_profileResponse.setName(tmp_profile.getName());
            tmp_profileResponse.setCard_number(tmp_profile.getCard_number());
            tmp_profileResponse.setPhone(tmp_profile.getPhone());
            tmp_profileResponse.setShop_name(tmp_profile.getShop_name());
            tmp_profileResponse.setBase64StringImage(encoder(tmp_profile.getImage_path()));
            tmp_profileResponse.setKota_kab(g_kotaKabupatenRepository.findById(tmp_profile.getId_kab_kota()));
            tmp_profileResponse.setSelected_courier(g_selectedCourierRepository.getSelectedCourier(id_seller));

            l_output = new ResultEntity(tmp_profileResponse, ErrorCode.BIT_000);

        }catch (Exception e){
            l_output = new ResultEntity(e.toString(), ErrorCode.BIT_999);
        }

        return l_output;
    }

    public ResultEntity updateProfile(ProfileRequest p_profileRequest){
        String FULL_PATH = "";
        String tmp_Shopname = p_profileRequest.getShop_name();
        int tmp_kabKotaId = p_profileRequest.getId_kota_kab();
        int l_sellerId = p_profileRequest.getId_seller();

        //Delete image if exist
        String tmp_unusedImagePath = g_profileRepository.getImagePathBySellerId(p_profileRequest.getId_seller());
        File tmp_file = new File(tmp_unusedImagePath);
        if (tmp_file.exists()){
            try{
                tmp_file.delete();
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }

        //Check image request
        if (!p_profileRequest.getBase64StringImage().equals("")){
            FULL_PATH = saveImage(p_profileRequest);
        }

        //Update profile data
        if (!FULL_PATH.equals("failed") || !FULL_PATH.equals("")){
            g_profileRepository.updateProfileById(tmp_Shopname, tmp_kabKotaId, FULL_PATH, l_sellerId);
        }else{
            g_profileRepository.updateProfileById(tmp_Shopname, tmp_kabKotaId, "", l_sellerId);
        }

        if (p_profileRequest.getSelected_courier()!=null || p_profileRequest.getSelected_courier().size()!=0){
            for (int i=0; i<p_profileRequest.getSelected_courier().size(); i++){
                int tmp_courierId = p_profileRequest.getSelected_courier().get(i).getId_courier();
                int tmp_isSelected = p_profileRequest.getSelected_courier().get(i).getIs_selected();

                g_selectedCourierRepository.updateSelectedCourier(tmp_isSelected, l_sellerId, tmp_courierId);
            }
        }

        return new ResultEntity("Y", ErrorCode.BIT_000);
    }

    public ResultEntity changePass(ChangePassword changePassword){
        String oPass = g_profileRepository.getPassById(changePassword.getId_seller());

        String oldPass = changePassword.getO_password();
        String newPass = changePassword.getN_password();
        String conPass = changePassword.getC_password();
        Integer id = changePassword.getId_seller();

        if(!new BCryptPasswordEncoder().matches(oldPass, oPass)){
            System.out.println("Old Password: " + oldPass);
            return new ResultEntity("Old password is wrong", ErrorCode.BIT_999);
        }
        else if(newPass.equals(conPass) == false)
        {
            System.out.println("New Password: " + newPass);
            return new ResultEntity("New password did not match", ErrorCode.BIT_999);
        }
        else{
            try{
                System.out.println("id: "+id);
                System.out.println("pass: "+newPass);
                g_profileRepository.updatePassById(id, new BCryptPasswordEncoder().encode(newPass));
                return new ResultEntity("Success change password", ErrorCode.BIT_000);
            }
            catch (Exception ex){
                ex.printStackTrace();

                return new ResultEntity("Service undermaintenance", ErrorCode.BIT_999);
            }
        }
    }

    public String encoder(String p_imagePath) {
        String tmp_base64Image = "";
        System.out.println(p_imagePath.substring(8));
        String imagePath = "\\NASBOS\\" + p_imagePath.substring(8);
        File tmp_file = new File(imagePath);
        try (FileInputStream tmp_imageInFile = new FileInputStream(tmp_file)) {
            // Reading a Image file from file system
            byte tmp_imageData[] = new byte[(int) tmp_file.length()];
            tmp_imageInFile.read(tmp_imageData);
            tmp_base64Image = Base64.getEncoder().encodeToString(tmp_imageData);
            return tmp_base64Image;

        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
            return "";

        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
            return "";
        }
    }
}
