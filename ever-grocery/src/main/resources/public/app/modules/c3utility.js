define(['knockout', 'd3'], function (ko, d3) {
    return {
    	getDefaultAxis: function() {
    		return {
		    	x: {
		    		type: 'category',
		    		tick: {
		    			rotate: -30,
		    			multiline: false,
		    			culling: {
		    		        max: 13
		    		    }
		    		},
		    		height: 70
		    	},
		    	y: {
		    		tick: {
		    			format: d3.format(",")
		    		}
		    	}
		    };
    	}
    };
});