---
title: "Project Overview"
permalink: /project-overview/
header:
  overlay_color: "#000"
  overlay_filter: "0.5"
  overlay_image: /assets/images/banner_light1.jpg
---

# Weight Tracking Application

## Project Introduction 

The Computer Science program's capstone project involved revisiting any particular project completed in a previous course and conducting a thorough code review. This code review intended to provide the opportunity to identify areas for improvement and to clearly explain what the application does and how it is able to do so. We were then asked to implement three enhancements into the project, where each enhancement represents a certain category or criteria. The three categories for these enhancement are:
- Software Design
- Data Structures and Algorithms
- Databases

The artifact I chose is a mobile app that was initially programmed in Java and created in Android Studio. The application in a broad sense was a weight tracking application that provided users with a way to add weight records to a list specific to their account. The application did provide login and registration functionality, so a user would be able to create a profile and login to see records specific to their account. The app utilized an in-memory database using Room, which is built on top of SQLite, so the user’s information and weight records were stored in it. Additionally, the user could set a target weight from the main screen. The application would send out an SMS and push notification when the user reached their target weight.

### Original Implementation
The original project's implementation included:
- Java development in Android Studio
- In-memory database for local storage using Room database
- Activity-based navigation follow MVVM architecture
- Standard CRUD operations for weight records

## Code Review
This video provides a thorough code review of my original project selected as my "Artifact" with which I would be implementing the three enhancements. 

<div class="video-container">
  <iframe 
    width="100%" 
    height="315" 
    src="https://www.youtube.com/embed/G5xlPHrt3JQ"
    title="YouTube video player"
    frameborder="0"
    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture, web-share" 
    referrerpolicy="strict-origin-when-cross-origin"
    allowfullscreen>
  </iframe>
</div>

## Enhancements Overview
<div class="enhancement-cards">
    <a href="/enhancements#software-design" class="card">
        <div class="card-content">
            <h3>Software Design</h3>
            <i class="fas fa-code fa-3x"></i>
            <p>Java to Kotlin conversion, implementing modern Android architecture components including FragmentContainerView and Navigation.</p>
            <span class="learn-more">Learn More →</span>
        </div>
    </a>

    <a href="/enhancements#algorithms" class="card">
        <div class="card-content">
            <h3>Algorithms & Data Structure</h3>
            <i class="fas fa-chart-line fa-3x"></i>
            <p>Implementation of MPAndroidChart for visual analytics and enhanced data sorting algorithm.</p>
            <span class="learn-more">Learn More →</span>
        </div>
    </a>

    <a href="/enhancements#database" class="card">
        <div class="card-content">
            <h3>Database</h3>
            <i class="fas fa-database fa-3x"></i>
            <p>Migration to Firebase Realtime Database and implementing Firebase Authentication for security.</p>
            <span class="learn-more">Learn More →</span>
        </div>
    </a>
</div>
