---
title: "Professional Self-Assessment"
permalink: /self-assessment/
header:
  overlay_color: "#000"
  overlay_filter: "0.5"
  overlay_image: /assets/images/banner_orange.jpg
---

# Professional Self-Assessment

## Introduction

Hello! 

My name is Ryan Hoskins and I am a passionate and dedicated Software Developer that has spent the last four years obtaining an Associate in Science, Associate in Engineering, and Bachelor of Science degree in Computer Science degree in Computer Science with a concentration on Software Engineering. Throughout my academic pursuits, I have continued to work full-time as a Product Support Expert for one of the largest Media Monitoring and Media Intelligence companies, where I collaborate across teams and assisted Engineers in day-to-day processes. Over the course of the Computer Science program, I have been exposed to a variety of technologies and principles ranging from Full-stack Development, software reverse engineering, artificial intelligence and machine learning, as well as mobile application development. I have also had the chance to hone my skills and expertise in software engineering, data structures and algorithms, and database management. This portfolio is meant to demonstrate my growth over the course of this program and address several key course outcomes from the Computer Science Capstone course.  

## Course Outcomes

![Course Outcomes](/assets/images/course_outcome.png){: .full}

### Collaborating In Team Environments
Software development often heavily relies on collaborating in a team environment, whether that means through workflows, general communication, or taking feedback into consideration. Throughout my time in this degree and professional career, I have been required to embrace working in a collaborative environment. This can be seen especially in my general work. I often have to collaborate with other Engineers in our company to help identify, document and escalate severity issues, but this is also a common practice when I am developing documentation for other departments to use. I am responsible for drafting and polishing much of the training material used internally for various workflows, but I have also done the same for user-facing documentation. This is also a common practice in my academic career, as I have needed to gather feedback on a project I’m working on to ensure requirements are met or for designing the User Interface of a system. An example of this can be seen in my work from CS-319. 

  <a href="https://github.com/ryan-m-hoskins/CS319" class="btn btn--primary btn--large">CS-319 Project Repository <i class="fab fa-github"></i></a>
  
### Communicating With Stakeholders 
The ability to communicate the relevant stakeholders is an essential skill to have. Communicating technical concepts to a non-technical audience is an ever-present need, especially in the Computer Science field. This is something I have to do on an almost daily basis with clients and users, but is also something I have needed to do all throughout my academic career. I’m particularly thorough and detail-oriented, which are both characteristics that can be seen in my work. In CS-465, the full-stack development course, I was tasked with creating a technically sound and accurate UML diagram of the Single Page Application we would be developing using Angular. 

![Travlr UML Diagram](/assets/images/travlr_UML.png){: .align-center}
*Full-stack application architecture diagram created in CS-465*{: .text-center}

I’ve also been tasked with developing a simple and easy-to-understand presentation that summaries a Security Policy Report I created for a client in CS-405. In this, I had to be brief and avoid being overly technical to ensure the message and core concepts were accurately communicated. 

  <a href="https://github.com/ryan-m-hoskins/CS405" class="btn btn--primary btn--large">CS-405 Security Policy Report <i class="fab fa-github"></i></a>

### Data Structures and Algorithms
The choice of what sort of data structure is used or what algorithm is employed can have a profound impact on the performance of a program. Determining the appropriate solution can also rely on carefully managing trade-offs in design choices. This is a decision I encountered often in my experience in the Computer Science program and especially in the enhancements I implemented in the Capstone project. The data structures and algorithms enhancement I made showcases my ability to optimize a system’s performance. I implemented sorting functionality in the mobile application’s RecyclerView to ensure the weight records displayed are organized based on date. This required that I use Kotlin’s sortByDescending function:
```kotlin
class WeightRecordAdapter(
    private val onItemClick: (WeightRecord) -> Unit
) : RecyclerView.Adapter() {
    private var weightRecords = listOf()

    fun updateRecords(newRecords: List) {
        weightRecords = newRecords.sortedByDescending { it.date }
        notifyDataSetChanged()
    }
}
```
{: .language-kotlin}

Additionally, I migrated from a basic Activity-based structure to a more sophisticated and efficient Fragment-based implementation. All of these demonstrate an understanding of data structures and algorithms to optimize a system’s performance, while still considering the design choices. 

### Software Engineering and Database
One of the most significant enhancements I made for the capstone project involved the transition of the original in-memory database to Firebase Realtime Database. This transition provides significantly more value than the original in-memory database through Room provided, as it allows a user to access the same data across any device, supports real-time updates, and still support offline caching to ensure the user isn’t limited to an application that only functions online. This shift addressed the crucial limitation of the original application’s implementation – a lack of persistence and scalability. The database also ties into the data structures criteria as well, as instead of relying on a traditional structured database, this form of a database is adaptable, flexible, and utilizes a JSON structure. The full database implementation and the CRUD functionality can be seen here. 

![Database Structure](/assets/images/db_structure.png){: .align-center}
*Firebase Realtime Database structure showing JSON-based organization*{: .text-center}

Another significant enhancement can be seen in the transition from the original architecture, which followed an MVVM system architecture, to a fragment based architecture that utilized a NavGraph and Bottom Navigation. 

### Security
The importance of security and user privacy in the field of computer science continues to grow, which is why it was a core consideration in all enhancements of the capstone project. To address the most significant of privacy and security concerns, I implemented Firebase Auth for the registration and login functionality of the application. This ensures any users are authorized and their email and password are authenticated. This enhancement also addresses the concern of sensitive user data, as it is securely stored in the Firebase Realtime Database, which utilizes rules to restrict access based on authentication status. 
![Database Rules](/assets/images/db_rules.png){: .align-center}
*Firebase security rules implementation*{: .text-center}

  Additionally, I utilized defensive programming to validate user inputs that also handles edge cases. This ensures the application functions as intended and edge cases are addressed. 

  <a href="https://github.com/ryan-m-hoskins/CS499_WTA/blob/cs499_v2.4/app/src/main/java/com/example/cs499_app/BottomSheetEditWeight.kt" class="btn btn--primary btn--large">Capstone Project Repository <i class="fab fa-github"></i></a>
  

