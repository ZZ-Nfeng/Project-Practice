package com.zhi.delivery.dto;


import com.zhi.delivery.entity.Setmeal;
import com.zhi.delivery.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
