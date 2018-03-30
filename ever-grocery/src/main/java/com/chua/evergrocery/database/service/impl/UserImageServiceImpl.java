package com.chua.evergrocery.database.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.evergrocery.database.dao.UserImageDAO;
import com.chua.evergrocery.database.entity.UserImage;
import com.chua.evergrocery.database.service.UserImageService;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 30, 2018
 */
@Service
public class UserImageServiceImpl
		extends AbstractService<UserImage, Long, UserImageDAO> 
		implements UserImageService {

	@Autowired
	protected UserImageServiceImpl(UserImageDAO dao) {
		super(dao);
	}

	@Override
	public List<UserImage> findAllByUserId(Long userId) {
		return dao.findAllByUserId(userId);
	}
}
