package com.dxl.delivermanservice.dao;


import com.dxl.delivermanservice.enumoperation.DeliverymanStatus;
import com.dxl.delivermanservice.po.DeliverymanPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeliverymanMapper {

    @Select("SELECT id,name,status,date FROM deliveryman WHERE id = #{id}")
    DeliverymanPO selectDeliveryman(Integer id);

    @Select("SELECT id,name,status,date FROM deliveryman WHERE status = #{status}")
    List<DeliverymanPO> selectAvaliableDeliveryman(DeliverymanStatus status);
}
