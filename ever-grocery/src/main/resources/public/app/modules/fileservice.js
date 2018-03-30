define(['jquery', 'fileDownload'], function ($, fd) {
	return {
		downloadGeneratedPurchaseByFileName: function(fileName) {
			fd.fileDownload('/services/file/generatedpurchase/' + fileName)
			return false;
    	},
    	
    	downloadInventoryByFileName: function(fileName) {
			fd.fileDownload('/services/file/inventory/' + fileName)
			return false;
    	}
	};
});
