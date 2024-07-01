package org.dessert.moah.dto.order;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.AnyDiscriminator;

@Getter
@Builder
public class OrderRequestDto {
    private Long dessertId;
    private int count;
}
