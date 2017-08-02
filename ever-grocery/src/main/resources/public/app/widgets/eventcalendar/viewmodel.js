define(['jquery', 'knockout', 'moment', 'fullcalendar'], function ($, ko, moment) {
	var EventCalendar = function() {
		this.calendar = null;
		
		this.calendarId = null;
		this.height = null;
		this.width = null;
		this.defaultDate = null;
		
		this.getEventList = null;
	};
	
	EventCalendar.prototype.initialize = function() {
		var self = this;
		
		self.calendar = $('#' + self.calendarId);
		self.calendar.fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'prev,next today'
			},
			height: self.height,
			width: self.width,
			defaultDate: self.defaultDate,
			eventLimit: true
			//eventColor: '#b3e0ff' eventBackgroundColor: '#FFF' eventTextColor: '#000' eventBorderColor: '#FFF'
		});
	};
	
	EventCalendar.prototype.activate = function(settings) {
		var self = this;
		
		self.calendarId = settings.config.calendarId;
		self.height = settings.config.height;
		self.width = settings.config.width;
		self.defaultDate = settings.config.defaultDate;
		self.getEventList = settings.config.getEventList;
	};
	
	EventCalendar.prototype.attached = function() {
		var self = this;
		
		self.initialize();
		
		var view = self.calendar.fullCalendar('getView');
		$('.fc-prev-button').click(function() {
			self.getEventList(view.start, view.end, function(result) {
				self.loadEventList(result);
			});
		});
		
		$('.fc-next-button').click(function() {
			self.getEventList(view.start, view.end, function(result) {
				self.loadEventList(result);
			});
		});
		
		$('.fc-today-button').click(function() {
			self.getEventList(view.start, view.end, function(result) {
				self.loadEventList(result);
			});
		});
		
		self.getEventList(view.start, view.end, function(result) {
			self.loadEventList(result);
		});
	};
	
	EventCalendar.prototype.loadEventList = function(eventList) {
		var self = this;
		
		self.calendar.fullCalendar('removeEvents');
		self.calendar.fullCalendar('addEventSource', eventList);
	};
	
    return EventCalendar;
});