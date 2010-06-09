var printer = "";
reset = function() { printer = ""; }

//navigator
function _NAVIGATOR() {
	this.userAgent = "jfcrawler(THUIR-bot)";
}
var navigator      = new _NAVIGATOR();

//window
function _WINDOW() {
	this.self = this;
	this.top  = this;
	this.document = new _DOCUMENT(this, new Object(), new Object());
}
var window = new _WINDOW();

var top    = window;
var self   = window;
var parent = window;

//document
function _DOCUMENT(_window, _obj, _cookie) {
	this.cookie    = _cookie;
	this.frame     = _obj;
	this.frames    = _obj;
	this.history   = _obj;
	
	this.document  = this;
	this.length    = 0;
	
	this.defaultStatus = "";
	this.location  = "";
	this.name      = "";
	this.status    = "";
	
	this.opener    = _window;
	this.parent    = _window;
	this.self      = _window;
	this.top       = _window;
	this.window    = _window;
	
	this.getElementById
		= function(id) {};
	this.write
		= function(s) {printer+=(s+'\n');};
}
var document      = new _DOCUMENT(window, new Object(), new Object());
