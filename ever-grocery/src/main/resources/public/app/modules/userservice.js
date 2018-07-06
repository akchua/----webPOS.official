define(['jquery'], function ($) {
	return {
		getUser: function(userId) {
			return $.ajax({
				url: '/services/user/get',
				data: {
					userId: userId
				}
			});
		},
		
		getUserImageByFileName: function(fileName) {
			return '/services/user/getimage/' + fileName;
		},
		
		getUserList: function(currentPage, searchKey) {
			return $.ajax({
				url: '/services/user/list',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey
				}
			});
		},
		
		getUserImageList: function(userId) {
			return $.ajax({
				url: '/services/user/imagelist',
				async: false,
				data: {
					userId: userId
				}
			});
		},
		
		retrieveUser: function(retrievalKey) {
			return $.ajax({
				url: '/services/user/userretrieval',
				data: {
					retrievalKey: retrievalKey
				}
			});
		},
		
		saveUser: function(userFormData) {
    		return $.ajax({
    			url: '/services/user/save',
    			method: 'POST',
    			data: {
    				userFormData: userFormData
    			} 
    		});
    	},
    	
    	uploadUserImage: function(userId, imageFile) {
    		var formData = new FormData();
    		formData.append('imageFile', imageFile);
    		formData.append('userId', userId);
    		
    		return $.ajax({
    			url: '/services/user/uploadimage',
    			method: 'POST',
    			data: formData,
    			cache: false,
    			contentType: false,
    			processData: false
    		});
    	},
    	
    	setUserImageAsThumbnail: function(userImageId) {
    		return $.ajax({
    			url: '/services/user/setthumbnail',
    			method: 'POST',
    			data: {
    				userImageId: userImageId
    			}
    		});
    	},
    	
    	removeUser: function(userId) {
    		return $.ajax({
    			url: '/services/user/remove',
    			method: 'POST',
    			data: {
    				userId: userId
    			}
    		});
    	},
    	
    	removeUserImage: function(userImageId) {
    		return $.ajax({
    			url: '/services/user/removeimage',
    			method: 'POST',
    			data: {
    				userImageId: userImageId
    			}
    		});
    	},
    	
    	changePassword: function(passwordFormData) {
    		return $.ajax({
    			url: '/services/user/changepassword',
    			method: 'POST',
    			data: {
    				passwordFormData: passwordFormData
    			} 
    		});
    	},
    	
    	resetPassword: function(userId) {
    		return $.ajax({
    			url: '/services/user/resetpassword',
    			method: 'POST',
    			data: {
    				userId: userId
    			}
    		});
    	},
    	
    	changeSettings: function(settingsFormData) {
    		return $.ajax({
    			url: '/services/user/changesettings',
    			method: 'POST',
    			data: {
    				settingsFormData: settingsFormData
    			} 
    		});
    	},
    	
    	getUserListByUsername: function() {
    		return $.ajax({
    			url: '/services/user/listbyusername'
    		});
    	},
    	
    	getUserTypeList: function() {
    		return $.ajax({
    			url: '/services/user/listusertype'
    		});
    	},
    	
    	getUserListOrderByName: function() {
    		return $.ajax({
    			url: '/services/user/userlist'
    		});
    	},
    	
    	getLesserUserListOrderByName: function() {
    		return $.ajax({
    			url: '/services/user/lesseruserlist'
    		});
    	},
    	
    	getManagerList: function() {
    		return $.ajax({
    			url: '/services/user/managerlist'
    		});
    	}
	};
});