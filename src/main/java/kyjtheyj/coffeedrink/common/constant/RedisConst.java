package kyjtheyj.coffeedrink.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConst {
    public static final String REFRESH_TOKEN_PREFIX = "Refresh-Token:";
    public static final String BLACK_LIST_PREFIX = "BlackList:";
    public static final String RANKING_KEY_PREFIX = "menu:ranking:";
    public static final String RANKING_RESULT_KEY_PREFIX  = "menu:ranking:result:";
}
