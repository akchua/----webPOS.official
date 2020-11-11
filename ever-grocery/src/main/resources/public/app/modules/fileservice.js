define(['jquery', 'fileDownload'], function ($, fd) {
	return {
		downloadGeneratedPurchaseByFileName: function(fileName) {
			fd.fileDownload('/services/file/generatedpurchase/' + fileName)
			return false;
    	},
    	
    	downloadGeneratedOfftakeByFileName: function(fileName) {
			fd.fileDownload('/services/file/generatedofftake/' + fileName)
			return false;
    	},
    	
    	downloadCurrentPromoByFileName: function(fileName) {
    		fd.fileDownload('/services/file/currentpromo/' + fileName)
    		return false;
    	},
    	
    	downloadInventoryByFileName: function(fileName) {
			fd.fileDownload('/services/file/inventory/' + fileName)
			return false;
    	},
    	
    	downloadSalesReportByFileName: function(fileName) {
			fd.fileDownload('/services/file/salesreport/' + fileName)
			return false;
    	},
    	
    	downloadBackendReportByFileName: function(fileName) {
			fd.fileDownload('/services/file/backendreport/' + fileName)
			return false;
    	},
    	
    	downloadJournal: function() {
			fd.fileDownload('/services/file/journal')
			return false;
    	}
	};
});
