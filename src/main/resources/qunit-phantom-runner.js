if (phantom.args.length === 0 || phantom.args.length > 2) {
    console.log('Usage: qunit-phantom-runner.js URL');
    phantom.exit();
}

var url = phantom.args[0];

var page = new WebPage();

page.onAlert = function(msg) {
	console.log(msg);
};

page.onError = function (msg, trace) {
    console.log("ERROR: " + msg);
    
    trace.forEach(function(item) {
        console.log('  ', item.file, ':', item.line);
    })
    
    phantom.exit(1);
};

page.onInitialized = function() {
	page.evaluate(function() {
		window.document.addEventListener( "DOMContentLoaded", function() {
			
			//these prefixes are read by the ant task, update the phantom runner if changing them
			var PASS_PREFIX = "PASS: ";
			var FAIL_PREFIX = "FAIL: ";
			
			QUnit.moduleStart(function(module) {
				alert("Running module: " + module.name);
			});
			
			QUnit.moduleDone(function(result) {
				alert("Module done. Passed: " + result.passed + "/" + result.total + ". Failed: " + result.failed + "/" + result.total + ".");
			});
			
			QUnit.testStart(function(test) {
				alert("Running test: " + test.name)
			});
			
			QUnit.testDone(function(result) {
				alert("Test done. Passed: " + result.passed + "/" + result.total + ". Failed: " + result.failed + "/" + result.total + ".");
			});
			
			QUnit.log = function(test) {

				var message = test.message || "";
		    	if(test.result) {
		    		alert(PASS_PREFIX + message);
		    	} 
				else {
		    		alert(FAIL_PREFIX + message + ". Expected [" + test.expected + "] but was [" + test.actual + "].");
		    	}
			};

			QUnit.done(function(result){
				window.qunitDone = true;
			});			

		}, false );	
	});
};

console.log("Running tests in file: " + url + "...");
page.open(url, function(status){
	
	if (status !== "success") {
		console.log("Unable to access network: " + status);
		phantom.exit(1);
	} else {
		var interval = setInterval(function() {
			
			
			if (finished()) {
				clearInterval(interval);
				phantom.exit();
			}
		}, 100);
	}
});

function finished() {
	return page.evaluate(function(){
		return window.qunitDone;
  });
};