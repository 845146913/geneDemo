package com.example.demo.support.enumeration;

import com.example.demo.support.res.page.Order;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * enum抽象类
 *
 * @param <E>
 */
public interface GenericEnum<E extends Enum<E>, PK> {
    /**
     * E
     * @return E
     */
    @SuppressWarnings("unchecked")
    default E get() {
        E e = (E)this;
        return e;
    }

    /**
     * value() 子类需要实现它获取自定义value变量
     * @return
     */
    PK value();
    /**
     * desc()
     * default name()
     * @return
     */
    default String desc() {
        return this.get().name();
    }

    /**
     * 通过value变量获取枚举值
     * SUCCESS，ERROR这种value表示为ordinal下标，其余都为value变量获取
     * @param clazz
     * @param i
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<E>, PK> E get(Class<E> clazz, Object i) {
        if(null == i || "".equals(i))
            return null;
        int count = 0;
        try {
            for(Field f : clazz.getDeclaredFields()) {
                if(!f.isEnumConstant() && !f.getType().getTypeName().contains(clazz.getName()))
                    count ++;
            }
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        for(E e : clazz.getEnumConstants()) {
            if ( count > 0 ) {
                GenericEnum<E, PK> o = (GenericEnum<E, PK>) e;
                if( Objects.equals(o.value(), i) )
                    return e;
            } else if (Objects.equals(e.ordinal(), i)) {
                return e;
            }

        }
        throw new IllegalArgumentException(
                "No enum constant " + clazz.getName() + "." + i);
    }

}
