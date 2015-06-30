# Quick Links

* [Demo](#demo)
* [Installation](#installation)
  * [Dependencies](#dependencies)
  * [Local Tomcat7](#local-tomcat7)
  * [Hosted](#hosted)
* [About Claire](#about-claire)

## Demo
Want to see some interesting relationships between adverse events or recalls and Twitter? Check out the correlation between negative tweets and a recall with Amlodipine or between a cluster of adverse events and negative tweets with Wellbutin.

## Installation

This Installation Guide is intended for *nix machines.

Github repo: https://github.com/42sixsolutions/claire

### Dependencies

* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)
* [Maven](https://maven.apache.org/download.cgi)
* [NodeJs](https://nodejs.org/download/)
* [FireFox](https://www.mozilla.org/en-US/firefox/new/)

Claire can be installed on either a local machine or in a hosted environment. Instructions for both methods are included below.

### Local Tomcat7
The local Tomcat7 installation only works with HTTPS.
Type the following in a terminal window.
```
git clone https://github.com/42sixsolutions/claire
cd claire/
mvn clean install
  war file is in: claire-web/target/claire.war
cd claire-web/
mvn tomcat7:run
```
Using a browser, navigate to the site:
```
https://localhost:8443/#/
	Will only be accessible through https
	If a warning saying “Your connection is not private” comes up:
		Click "Advanced"
		Click “Proceed to localhost (unsafe)”
```

### Hosted
```
Select your IaaS option:
	openshift.com (Free)
	ARC (Paid)
On Openshift (the PaaS for both IaaS options):
  create new application button
	tomcat 7
	set properties
		public URL
		git url: https://github.com/42sixsolutions/claire.git
		branch: master
		size: small
		no scaling
		pick a region
	create application
```

## About Claire

Please see our [About Page.](http://42sixsolutions.github.io/claire/)

You can also view our [architecture diagram](claire_diagram.jpg), [design assets](claire-ui/mockups), [style guide](claire-ui/styleguide), and [scrum artifacts](claire-agile/) in the Claire repository.
