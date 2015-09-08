[![Build Status](https://travis-ci.org/42sixsolutions/claire.svg?branch=master)](https://travis-ci.org/42sixsolutions/claire)

# URLs to Prototpye
Demo on OpenShift: https://clairedemo-42six.rhcloud.com/

Demo on ARC: https://demo-claire.arcwrx.com/

# Overview
Claire is a product for drug manufacturing marketers, drug researchers, and the general public. It is designed for web and mobile interfaces. The system correlates adverse events for drugs, consumed from FDA data sources, to positive and negative sentiments expressed on Twitter for that drug. The results are aggregated by drug and displayed in user-friendly graphs. Claire may be used to 

1. alert manufacturers to negative feedback about their drug that may lead to reductions in sales
1. allow researchers to investigate if negative customer feedback lead to drug recalls
1. inform the general public on drugs to avoid based on recalls and adverse events
1. educate the general public on whether they should consider using a specific drug based on what other users are saying

Claire uses text-searching algorithms to identify positive or negative sentiment associated with the drug in question in every collected tweet, and plots the sentiment against the adverse events and recalls on a time-series line chart. The backend architecture consists of a flat-file and a web framework deployed on OpenShift. The frontend is written in Javascript and uses AngularJS, JQuery, and FLOT to create the data visualizations.  

Claire can be hosted on both Autonomic Resources FedRAMP-certified Cloud (ARC) or OpenShift Public PaaS. 

# Our Process
Our agile processes used in the development of Claire are aligned with U.S. Digital Services Playbook’s 13 “Plays” as documented in the repository and aligned with evidence criteria. The evidence criteria are in the sections below.

## Assigned One Leader 
CSC management assigned Brian Murphy as the product owner for this project.   Brian currently manages five agile projects and is ultimately responsible for delivery, ongoing success,  continuous improvement, and quality of these projects, including this GSA Agile Delivery prototype.  Accountability is maintained through updates to senior leadership after each sprint cycle.

##Assembled a Multidisciplinary and Collaborative Team
The Claire agile team utilized eight BPA labor categories: product manager, technical architect, back end developer, front end developer, visual designer, interaction designer, writer, and business analyst. The team also used an FDA subject matter expert that provided valuable end-user insight to drive the requirements, conducted usability testing and provided valuable feedback. The team met in a conference room that was converted into a "war room" for the duration of the project.

## Understand What People Need
We included industry experts in our process and engaged them in our Discovery Workshop by collaborating real-time via sketching and wireframing. We also included them in usability testing via paper prototyping and demonstrations. We incorporated feedback from the experts into the initial version of the prototype, and we incorporated additional feedback after our demonstrations.

## Use “Human-Centered Design” Techniques or Tools
The CSC agile process embraces the Interface Driven Development (IDD) process. IDD puts the user first. We start with features and enhancements by first working on the user interface, which drives requirements gathering and also helps determine the architecture. The UX Engineer facilitates and guides the teams from the start. We conduct discovery workshops to collaborate with the customer on high-level requirements, user experience workflow, and user interfaces. Our UX Engineer continues to flesh out user experience and wireframes, with collaboration from internal development teams. We conduct customer reviews of designs and connect our UX process to our Acceptance Test Driven Development (ATDD) model. The result is stories with built-in design and testable criteria to drive our definition of done. Every agile team has a UX/front-end developer to implement code design and iteratively advance the user experience.  Once we have a sufficient backlog, we start our development sprint(s).

For Claire, we used the following IDD techniques and tools:
 
1. Held Discovery/Brainstorming Workshop
1. Identified and interviewed users and create user personas
1. Visualized requirements via rapid prototyping by creating sketches and wireframes
1. Conducted user testing
1. Ensured a consistent user experience across devices, browsers and platforms using responsive techniques
 
[Results of human-centered design](claire-ui/mockups)

s1 contains the initial design. s2 contains the second design iteration.

## Created or Used a Design Style Guide and/or a Pattern Library
We’ve utilized our own home-brewed responsive HTML & CSS library. We’ve also developed a product specific style guide for future development efforts.
 
[Style Guide](claire-ui/styleguide)

## Performed Usability Tests with People
In addition to testing the app with people outside of the development team, we held two demos with stakeholders and other users. We tested the application in an iterative manner, via paper prototyping, in addition to testing it during stakeholder and sprint demos.
 
[War room after Demo](claire-agile/War_Room_2.JPG)

[Design evolution, based on user feedback](claire-ui/mockups)

## Used an Iterative Approach
For Claire, the team used the Scrum process. The team held three two-day sprints, and met for 15-minute daily scrums. The team also held a sprint planning meeting at the beginning of each sprint and a combined sprint review and retrospective at the end of each sprint. This iterative approach helped us prioritize features (e.g., seeing the tweets associated with a sentiment spike) and improve the usability of the interface (e.g., modifying the chart colors). The product and sprint backlogs were managed using a combination of a physical scrum board for the product backlog and sprint progress and the GitHub issue tracker for technical tasks. The product owner and tech lead continuously groomed the product backlog. The team conducted many informal demonstrations and two formal review demonstrations to garner feedback from the stakeholder community.  
 
[War room and scrum board pictures](claire-agile)

[Product backlog and technical tasks](https://github.com/42sixsolutions/claire/issues)

## Created a Responsive Prototype that Works on Multiple Devices
The prototype has been tested with the following browsers and hardware:

1. Chrome on Windows, Mac, iPhone 6, iPad 2, and Android
1. Internet Explorer 11 on Windows
1. Firefox on Windows and Mac
1. Safari on Mac, iPhone 6, iPad 2, and Android
 
The prototype works best on Chrome on desktops/laptops and Android.

## Use at Least Five Modern and Open Source Technologies
We used the following modern and open source technologies (versions included):
 
1. jQuery 1.10.2
1. AngularJS 1.2.28
1. Bower 1.2.13
1. Flot 0.8.3
1. Sass 3.4.13
1. Trianglify 0.2.1
1. D3 3.4.13
1. Openshift 2.0
1. Tomcat 7.0.62
1. Maven 3.3.3
1. Travis CI
1. Jenkins 1.599
 
[Licenses](LICENSES.md)

## Deployed Prototype on an IaaS or PaaS
We deployed Claire on OpenShift and on our Autonomic Resources FedRAMP-certified Cloud (ARC). On each PaaS, we deployed a development version, which is directly connected to the continuous deployment tools, and a demonstration version, which is snapshot of the prototype. The demo URLS are at the top of the README.

Dev on OpenShift: https://claire-42six.rhcloud.com/

Dev on ARC: https://dashboard-claire.arcwrx.com/

## Wrote Unit Tests
Unit and regression tests were automated using JUnit, Karma, and Jasmine. The tests are located in throughout the repository (e.g., [openFDAClientTest](claire-client/openfda-client/src/test/java/com/_42six/claire/openfda/OpenFDAClientTest.java) and [ResponseTranslatorTest](claire-web/src/test/java/com/_42six/claire/web/ResponseTranslatorTest.java)).

## Set Up or Used Continuous Integration and Continuous Deployment
Travis CI was used for continuous integration and deployment on OpenShift while Jenkins was used for continuous integration and deployment on ARC. Travis CI resides outside of OpenShift while Jenkins is set up as an OpenShift gear.
 
[Architecture of Prototype, including CI and CD](claire_diagram.jpg)

## Set Up or Used Configuration Management
We used GitHub for configuration management. All configuration items are stored in the repository.

## Set Up or Used Continuous Monitoring
The Claire teams utilized NewRelic within OpenShift to perform advanced continuous monitoring. New Relic is set up to monitor both development servers and both demo servers.
 
We also used Google Analytics to collect metrics on page hits and unique visitors, so that the application owner may analyze user types and user behavior.

[Architecture of Prototype, New Relic and Google Analytics](claire_diagram.jpg)

## Deploy Software in a Container
Claire is deployed on Tomcat 7 on OpenShift via JBoss EWS cartridges. OpenShift uses kernel isolation features to keep tenant processes separate.

## Provided Installation Documentation
[Installation Guide](INSTALLATION-GUIDE.md)

## Openly Licensed and Free of Charge
All components of the prototype and underlying platforms are openly licensed and free of charge.
 
[Licenses](LICENSES.md)

## What Makes us Different?
Claire demonstrates the capabilities of one of our many agile teams. CSC understands that not all projects can be confined to a single team or contractor. We are very successful at scaling agile across multiple teams with large government programs such as USCIS, EPA, and many of the intelligence agencies while utilizing the Scaled Agile Framework. In most cases, there are multiple organizations, contractors, and stakeholders for each project. Our agile culture is “badgeless,” viewing all stakeholders as valued team members striving toward the same goal; creating value for the end user through lean and efficient value streams.  Another discriminator CSC possesses is having its own FedRAMP-certified cloud called ARC.  When required by our customers, ARC can provide flexibility that goes beyond public and private PaaS.
