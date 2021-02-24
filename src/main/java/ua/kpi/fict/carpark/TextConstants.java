package ua.kpi.fict.carpark;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextConstants {

    public static final String REGEX_USERNAME = "^(?=.{5,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

    public static final String REGEX_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    public static final String REGEX_NAME = "^([a-zA-Z]{2,30}\\s*)+$";

    public static final String REGEX_NATIVE_NAME = "^([А-ЩЁЭЮЯ]+[а-яё-]*)+[а-я]+$";

    public static final String REGEX_BUS_MODEL = "^(\\w+\\s?)+\\w+$";

    public static final int BUS_CAPACITY_MIN = 10;

    public static final int BUS_CAPACITY_MAX = 60;

    public static final int ROUTE_NUMBER_MIN = 1;

    public static final int ROUTE_NUMBER_MAX = 999;
}
