package com.bos.profile.service;

import bca.bit.proj.library.base.ResultEntity;
import bca.bit.proj.library.enums.ErrorCode;
import com.bos.profile.model.ChangePassword;
import com.bos.profile.model.Profile;
import com.bos.profile.model.ProfileDetail;
import com.bos.profile.repository.ProfileRepository;
import com.bos.profile.repository.SelectedCourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository g_profileRepository;
    @Autowired
    SelectedCourierRepository g_selectedCourierRepository;

    private String saveImage(ProfileDetail p_profileDetail){
        String tmp_uploadLocation = "C:\\Users\\U067726\\Pictures\\BOS\\Profile";
        String tmp_fileName = p_profileDetail.getId_seller() + ".jpg";
        String tmp_fullPath = tmp_uploadLocation + File.separator + tmp_fileName;

        try(FileOutputStream tmp_imageOutFile = new FileOutputStream(tmp_fullPath)){
            byte[] tmp_imageByteArray = Base64.getDecoder().decode(p_profileDetail.getBase64StringImage());
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
            ProfileDetail tmp_profileDetail = new ProfileDetail();
            tmp_profileDetail.setId_seller(id_seller);
            tmp_profileDetail.setUsername(tmp_profile.getUsername());
            tmp_profileDetail.setName(tmp_profile.getName());
            tmp_profileDetail.setCard_number(tmp_profile.getCard_number());
            tmp_profileDetail.setPhone(tmp_profile.getPhone());
            tmp_profileDetail.setShop_name(tmp_profile.getShop_name());
            tmp_profileDetail.setImage_path(tmp_profile.getImage_path());
            tmp_profileDetail.setId_kab_kota(tmp_profile.getId_kab_kota());
            tmp_profileDetail.setSelected_courier(g_selectedCourierRepository.getSelectedCourier(id_seller));

            l_output = new ResultEntity(tmp_profileDetail, ErrorCode.BIT_000);

        }catch (Exception e){
            l_output = new ResultEntity(e.toString(), ErrorCode.BIT_999);
        }

        return l_output;
    }

    public ResultEntity updateProfile(ProfileDetail p_profileDetail){
        String FULL_PATH = "";
        String tmp_Shopname = p_profileDetail.getShop_name();
        int tmp_kabKotaId = p_profileDetail.getId_kab_kota();
        int l_sellerId = p_profileDetail.getId_seller();

        String tmp_unusedImagePath = g_profileRepository.getImagePathBySellerId(p_profileDetail.getId_seller());
        File tmp_file = new File(tmp_unusedImagePath);
        if (tmp_file.exists()){
            try{
                tmp_file.delete();
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }

        if (!FULL_PATH.equals("failed")){
            FULL_PATH = saveImage(p_profileDetail);
            g_profileRepository.updateProfileById(tmp_Shopname, tmp_kabKotaId, FULL_PATH, l_sellerId);
        }else{
            g_profileRepository.updateProfileById(tmp_Shopname, tmp_kabKotaId, "", l_sellerId);
        }

        if (p_profileDetail.getSelected_courier()!=null || p_profileDetail.getSelected_courier().size()!=0){
            for (int i=0; i<p_profileDetail.getSelected_courier().size(); i++){
                int tmp_courierId = p_profileDetail.getSelected_courier().get(i).getId_courier();
                int tmp_isSelected = p_profileDetail.getSelected_courier().get(i).getIs_selected();

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

        if(oPass.equals(oldPass) == false){
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
                g_profileRepository.updatePassById(id,newPass);
                return new ResultEntity("Success change password", ErrorCode.BIT_000);
            }
            catch (Exception ex){
                ex.printStackTrace();

                return new ResultEntity("Service undermaintenance", ErrorCode.BIT_999);
            }
        }
    }
}
