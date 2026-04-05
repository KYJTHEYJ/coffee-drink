package kyjtheyj.coffeedrink.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConst {
    public static final String POINT_CHARGE_LOG_TOPIC = "point.chargeLog";
    public static final String POINT_USE_LOG_TOPIC = "point.useLog";
    public static final String MENU_RANKING_TOPIC = "menu.ranking";
    public static final String ORDER_DATA_COLLECTION_TOPIC = "order.collection";
}
