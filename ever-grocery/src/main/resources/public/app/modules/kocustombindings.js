define(['knockout'], function (ko) {
    ko.bindingHandlers.numericText = {
		update: function(element, valueAccessor, allBindingsAccessor) {
			var value = ko.utils.unwrapObservable(valueAccessor());
			var precision = ko.utils.unwrapObservable(allBindingsAccessor().precision) || ko.bindingHandlers.numericText.defaultPrecision;
			var thousandsSeparator = ko.utils.unwrapObservable(allBindingsAccessor().thousandsSeparator) || ko.bindingHandlers.numericText.defaultThousandsSeparator;
			var formattedValue = value != undefined && !isNaN(parseFloat(value)) ? (value.toFixed(precision)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSeparator) : value;
			
			ko.bindingHandlers.text.update(element, function() {
				return formattedValue;
			});
		},
		defaultPrecision: 0,
		defaultThousandsSeparator: ","
    };
});