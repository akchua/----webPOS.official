define(['knockout'], function (ko) {
    return {
    	computeNetPrice: function(base, discount) {
    		return base - (base * discount / 100);
    	},
    	
    	computeSellingPrice: function(base, percentProfit) {
    		return this.round(base + (base * percentProfit / 100));
    	},
    	
    	computeNetProfit: function(base, selling) {
    		return selling - base;
    	},
    	
    	round: function(number) {
    		return (Math.ceil(number * 4)) / 4;
    	},
    	
    	mod: function(dividend, divisor) {
    		return (((dividend % divisor) + divisor) % divisor);
    	}
    };
});