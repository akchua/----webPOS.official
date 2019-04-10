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
    	
    	roundToCent: function(number) {
    		return (Math.ceil(number * 100)) / 100;
    	},
    	
    	ceilToThousand: function(value) {
    		return (Math.ceil(value / 1000)) * 1000;
    	},
    	
    	ceilToFiveHundred: function(value) {
    		return (Math.ceil(value / 500)) * 500;
    	},
    	
    	ceilToHundred: function(value) {
    		return (Math.ceil(value / 100)) * 100;
    	},
    	
    	mod: function(dividend, divisor) {
    		return (((dividend % divisor) + divisor) % divisor);
    	}
    };
});