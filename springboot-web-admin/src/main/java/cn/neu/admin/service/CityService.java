package cn.neu.admin.service;

import cn.neu.admin.bean.City;
import cn.neu.admin.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    @Autowired
    CityMapper cityMapper;

    public City getCityMapper(Integer id) {
        return cityMapper.findById(id);
    }

    public void saveCity(City city){
        cityMapper.insert(city);
    }
}
