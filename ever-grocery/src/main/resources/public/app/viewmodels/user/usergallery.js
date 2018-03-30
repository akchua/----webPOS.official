define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], 
		function (dialog, app, ko, userService) {
    var UserGallery = function(userId) {
    	this.userId = userId;
    	this.userImageList = ko.observableArray();
    	
    	this.userModel = {
			imagePath: ko.observable()
    	}
    };
    
    UserGallery.prototype.activate = function() {
    	var self = this;
    	
    	self.refreshUser();
    	self.refreshUserImageList();
    };
    
    UserGallery.show = function(userId) {
    	return dialog.show(new UserGallery(userId));
    };
    
    UserGallery.prototype.refreshUser = function() {
    	var self = this;
    	
    	userService.getUser(self.userId).done(function(user) {
    		self.userModel.imagePath(userService.getUserImageByFileName(user.image));
    	});
    };
    
    UserGallery.prototype.refreshUserImageList = function() {
    	var self = this;
    	
    	userService.getUserImageList(self.userId).done(function(userImageList) {
    		for(i = 0; i < userImageList.length; i++) {
    			userImageList[i].filePath = userService.getUserImageByFileName(userImageList[i].fileName);
    		}
    		self.userImageList(userImageList);
    	});
    };
    
    UserGallery.prototype.uploadUserImage = function(imageFile) {
    	var self = this;
    	
    	userService.uploadUserImage(self.userId, imageFile).done(function(result) {
    		if(result.success) {
    			self.refreshUser();
    			self.refreshUserImageList();
    		}
    		app.showMessage(result.message);
    	});
    };
    
    UserGallery.prototype.setUserImageAsThumbnail = function(userImageId) {
    	var self = this;
    	
    	userService.setUserImageAsThumbnail(userImageId).done(function(result) {
    		if(result.success) {
    			self.refreshUser();
    		} else {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    UserGallery.prototype.removeUserImage = function(userImageId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove this image?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				userService.removeUserImage(userImageId).done(function(result) {
					self.refreshUser();
					self.refreshUserImageList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    UserGallery.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserGallery;
});
