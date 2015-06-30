# URLs to Prototpye

Prototype on OpenShift: https://claire-42six.rhcloud.com/

OpenShift Backup: https://clairedemo-42six.rhcloud.com/

Prototype on ARC: https://dashboard-claire.arcwrx.com/

ARC Backup: https://demo-claire.arcwrx.com/

Website: http://42sixsolutions.github.io/claire/

# 750 Word Project Description

## Overview
Claire is a product for drug manufacturing marketers, drug researchers, and the general public. It is designed for web and mobile interfaces. The system correlates adverse events for drugs, consumed from FDA data sources, to positive and negative sentiments expressed on Twitter for that drug. The results are aggregated by drug and displayed in user-friendly graphs. Claire may be used to

1. alert manufacturers to negative feedback about their drug that may lead to reductions in sales
1. allow researchers to investigate if negative customer feedback lead to drug recalls
1. inform the general public on drugs to avoid based on recalls and adverse events
1. educate the general public on whether they should consider using a specific drug based on what other users are saying

Claire uses text-searching algorithms to identify positive or negative sentiment associated with the drug in question in every collected tweet, and plots the sentiment against the adverse events and recalls on a time-series line chart. The backend architecture consists of a flat-file and a web framework deployed on OpenShift. The frontend is written in Javascript and uses Angular, JQuery, and FLOT to create the data visualizations.  

Claire can be hosted on both Autonomic Resources FedRAMP-certified Cloud (ARC) or OpenShift Public PaaS. 

## Our Team
The Claire agile team utilized a mix of seven BPA labor categories: product manager, technical architect, back end developer, front end developer, visual designer, interaction designer, and business analyst. The team also utilized an FDA subject matter expert that provided valuable end-user insight to drive the requirements, conducted usability testing and provided valuable feedback.

## Our Process
For Claire, the team used the Scrum process. The team held three two-day sprints, and met for 15-minute daily scrums. The team also held a sprint planning meeting at the beginning of each sprint and a combined sprint review and retrospective at the end of each sprint. The team applied the lessons learned in each sprint to improve both the prototype and the scrum process in the next sprint. The product and sprint backlogs were managed using a combination of a physical scrum board for the product backlog and sprint progress and the GitHub issue tracker for technical tasks. Unit and regression tests were automated using JUnit, Karma, and Jasmine. Travis CI was used for continuous integration and deployment on OpenShift while Jenkins was used for continuous integration and deployment on ARC. The product owner and tech lead continuously groomed the product backlog. The team conducted many informal demonstrations and two formal review demonstrations to garner feedback from the stakeholder community.  

The CSC agile process embraces the Interface Driven Development (IDD) process. IDD puts the user first. We start with features and enhancements by first working on the user interface, which drives requirements gathering and also helps determine the architecture. The UX Engineer facilitates and guides the teams from the start. We conduct discovery workshops to collaborate with the customer on high-level requirements, user experience workflow, and user interfaces. Our UX Engineer continues to flesh out user experience and wireframes, with collaboration from internal development teams. We conduct customer reviews of designs and connect our UX process to our Acceptance Test Driven Development (ATDD) model. The result is stories with built-in design and testable criteria to drive our definition of done. Every agile team has a UX/front-end developer to implement code design and iteratively advance the user experience.  Once we have a sufficient backlog, we start our development sprint(s). 

Our agile processes used in the development of Claire are aligned with U.S. Digital Services Playbook’s 13 “Plays” as documented in the repository and aligned with evidence criteria. The evidence criteria is documented in Attachment E which include the Design Pool and Development Pool criteria.  

## What Makes us Different?
Claire demonstrates the capabilities of one of our many agile teams. CSC understands that not all projects can be confined to a single team or contractor. We are very successful at scaling agile across multiple teams with large government programs such as USCIS, EPA, and many of the intelligence agencies while utilizing the Scaled Agile Framework. In most cases, there are multiple organizations, contractors, and stakeholders for each project. Our agile culture is “badgeless,” viewing all stakeholders as valued team members striving toward the same goal; creating value for the end user through lean and efficient value streams.  Another discriminator CSC possesses is having its own FedRAMP-certified cloud called ARC.  When required by our customers, ARC can provide flexibility that goes beyond public and private PaaS.
