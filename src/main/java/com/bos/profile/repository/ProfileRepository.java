package com.bos.profile.repository;


import com.bos.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE seller SET shop_name = :shop_name, id_kab_kota = :id_kab_kota, image_path = :image_path WHERE id_seller = :id_seller", nativeQuery = true)
    void updateProfileById(@Param("shop_name") String shop_name, @Param("id_kab_kota") int id_kab_kota, @Param("image_path") String image_path, @Param("id_seller") int id_seller);

    @Query(value = "SELECT image_path FROM seller WHERE id_seller = :id_seller", nativeQuery = true)
    String getImagePathBySellerId(@Param("id_seller") int id_seller);

    @Query(nativeQuery = true,
           value = "SELECT password FROM seller WHERE id_seller = :idSeller")
    String getPassById(@Param("idSeller") Integer idSeller);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
           value = "UPDATE seller SET password = :password WHERE id_seller = :id_seller")
    void updatePassById(@Param("id_seller") Integer idSeller, @Param("password") String pass);
}
