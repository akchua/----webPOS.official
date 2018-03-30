package com.chua.evergrocery.database.prototype;

import java.util.List;

import com.chua.evergrocery.database.entity.UserImage;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 30, 2018
 */
public interface UserImagePrototype extends Prototype<UserImage, Long> {

	List<UserImage> findAllByUserId(Long userId);
}
