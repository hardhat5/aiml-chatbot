# AIML Chatbot
A chatbot for helping a student decide on an elective course for the upcoming semester.

## Overview
* The chatbot is hosted on a Flask server
* The front end is an Android application
* AIML is used to parse the input and return outputs
* An SQLite database stores course names along with their domain and faculty

## Sub-tasks
* Random responses are used in multiple areas
* A persistent copy of interactions is stored in “persistent.txt”
* Courses recommended based on domain of interest and preferred faculty
* Github Jobs API is used to query and return the number of openings for “developer” job title

## How to get it running
* Modify ‘url’ in ‘MainActivity.java’ to the IP address of your machine
* Start the Flask Server using ‘python app.py’
* Start the Android emulator

## Sample
![Snap1](https://raw.githubusercontent.com/hardhat5/aiml-chatbot/master/Snap1.PNG "Snap1")
![Snap2](https://raw.githubusercontent.com/hardhat5/aiml-chatbot/master/Snap2.PNG "Snap2")
![Snap3](https://raw.githubusercontent.com/hardhat5/aiml-chatbot/master/Snap3.PNG "Snap3")

  