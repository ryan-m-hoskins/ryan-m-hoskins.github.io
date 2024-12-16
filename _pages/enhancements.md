
---
title: "Project Enhancements"
permalink: /enhancements/
header:
  overlay_color: "#000"
  overlay_filter: "0.5"
  overlay_image: /assets/images/banner_tri.jpg
toc: true
toc_label: "Enhancements"
toc_icon: "cog"
toc_sticky: true
---

# Three Artifact Enhancements

## Overview

The core of the Capstone Project involved revisiting a previous project that was completed earlier in the Computer Science degree and implementing three enhancements. The enhancements focused on:
- Software Design
- Data Structures and Algorithms
- Databases

The specific method or approach to implementing these enhancements were open to the developer to decide. Each enhancement listed will provide a summary of some of the major changes made, a narrative explaining the thought-process, specific changes, and obstacles encountered, as well as some examples showcasing the implementation. 

## Software Design & Engineering {#software-design}

This category intended to focus on improving an existing software, refactoring an existing project to another language, or expanding on the original project's complexity. My approach to resolving this category included:
- Converting the original project from Java to Kotlin
- Implementing modern Android architecture components (NavGraph, fragments, etc.)
- Improve data binding techniques and operational methods (Coroutines, Flow, etc.)
- Enhancing the user experience through added features

### Code Example (Kotlin)
```kotlin
class DatabaseRepository {
    private var weightRecordListener: ValueEventListener? = null
// Observing weight records in Firebase Realtime Database
    // Retrieve list of weight record objects, or error depending on outcome
    fun observeWeightRecords(onUpdate: (List<WeightRecord>) -> Unit, onError: (String) -> Unit) {
        // Get current user, return early if user is null
        val currentUser = auth.currentUser?.uid ?: return

        // Remove any existing listeners for weight record
        removeWeightRecordListener()

        // Creating a new listener for the value of the weight record
        weightRecordListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val weightRecords = mutableListOf<WeightRecord>()
                // Iterate through nodes in database until all weight records are found, adding each to a list
                for (recordSnapshot in snapshot.children) {
                    recordSnapshot.getValue(WeightRecord::class.java)?.let {
                        weightRecords.add(it)
                    }
                }
                // Passes list of records to onUpdate function
                onUpdate(weightRecords)
            }
            // Handles error if one occurs throughout process
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        }
        // Reference weight records of a user, set listener at node for any changes
        usersRef.child(currentUser).child("weight_records")
            .addValueEventListener(weightRecordListener!!)
    }

    // Cleanup of listener for weight record
    fun removeWeightRecordListener() {
        val currentUser = auth.currentUser?.uid ?: return
        weightRecordListener?.let { listener ->
            usersRef.child(currentUser).child("weight_records").removeEventListener(listener)
        }
        weightRecordListener = null
    }
}
```

### Narrative
I wanted to include this artifact in the portfolio as it presented a really great opportunity to demonstrate a variety of skills. For example, mobile app development is widely requested and in-demand. The app itself provides a lot of opportunity to implement the enhancements, ranging from migrating the database from an in-app database to a cloud-based database that utilizes authentication as well. Whether it be securing the user login experience or adopting a programming language that is growing to be an industry-standard, the artifact provides a lot of room to grow and demonstrate skills.

	The use of a RecyclerAdapter and database function as the primary components that provide this sort of opportunity. Showcasing an ability to refactor an existing program to another language provides a great chance to show my ability to quickly learn another language and update an existing project through a methodical and iterative manner. Additionally, addressing the app’s security concerns by adopting a cloud-based user authentication also demonstrates the ability to review potential issues areas of concern in an existing project and improving them dramatically. 
 
	The enhancements I’ve made so far include a partial refactoring of the code to use Kotlin instead of Java, as well as laying down the foundation of the Firebase Realtime Database and Firebase Authentication to facilitate the user login experience and data storage. The login screen, loginViewholder, TargetWeight, and the beginning of the DatabaseRepository have been refactored to Kotlin. I’ve also successfully connected the project to Firebase Auth and Firebase Realtime Database. This provides the app with a secure and dramatically improved database solution for the app. Firebase Realtime Database provide the user with a storage solution that is Real-time that would sync across any devices they are using – not just the single device they’re using that was previously supported with the first version of the app. 

Throughout the process of implementing these enhancements, I learned quite a lot. This started as a very daunting task that I had little to no experience in. I've never used Kotlin before, I've never used Firebase before, and I've never tried refactoring a project like this. So the process of implementing these enhancements presented quite a few challenges. I learned best-practices for Kotlin, which include the use of onBinding techniques for utilizing resources such as XML files. Firebase presented quite a few challenges, but it taught me how to read through documentation and adapting an app's design to accommodate the migration of the database. It was particularly challenging to figure out how to readjust the existing architecture from an MVVM architecture to one that utilizes a cloud-based system. 

### Incorporating Feedback
Part of the development process involved getting feedback from my instructor and taking those suggestions into consideration. Some of the feeedback they provided included:
- Error feedback
- Data visualization
- Input sanitization

Every process involving user input involves carefully validating their input, providing error message (which can be seen in the project demonstration video), and incorporating data visualization through the Analytics tab of the app. 

## Data Structures and Algorithms {#algorithms}

This category intended to focus on improving the efficiency of a software or expanding on the complexity of an existing data structure of algorithm. My approach to resolving this category included: 
- Integration of MPAndroidChart for data visualization
- Implementation of efficient sorting algorithms
- Optimizating data structure usage through the use of Fragments and JSON objects from the Firebase Database

### Code Example
```kotlin
class WeightRecordAdapter(
    private val onItemClick: (WeightRecord) -> Unit,
    ) : RecyclerView.Adapter<WeightRecordAdapter.WeightViewHolder>() {
    private var weightRecords = listOf<WeightRecord>()

    // Update records in the adapter in new order
    fun updateRecords(newRecords: List<WeightRecord>) {
        // Sort list of records based on date with most recent first
        weightRecords = newRecords.sortedByDescending { it.date }
        // Notify the adapter that the data has changed to update the RecyclerView
        notifyDataSetChanged()
    }
}
```
This implementation utilized each weight record's date to sort in a descending pattern, allowing the most recent records to be listed at the top. Additionally, the setup and updating of the analytics graph used in the app that was provided by MPAndroidChart also demonstrates incorporating the complexity utilizing the existing data structures to provide a visual graph for the user. 

```kotlin
// == Function to update chart with new data == //
    private fun updateChartData(records: List<WeightRecord>) {
        // First check if there are any records
        if (records.isEmpty()) return
        // If binding is null, return early
        if (_binding == null) return

        // Sort records by date
        val entries = records
            .sortedBy { it.date }
            .map { Entry(it.date.toFloat(), it.weight.toFloat()) }

        // Set the range of y-axis so it doesn't start at zero
        val minWeight = records.minOf {it.weight }.toFloat()
        val maxWeight = records.maxOf { it.weight }.toFloat()
        val yAxisRange = (maxWeight + 10.0)  - (minWeight - 10.0)

        // Declare min and max
        binding.weightChart.axisLeft.apply {
            axisMinimum = (minWeight - (yAxisRange * 0.1)).toFloat()
            axisMaximum = (maxWeight +  (yAxisRange * 0.1)).toFloat()
        }

        // Create a line chart with general styling
        val dataSet = LineDataSet(entries, "Weight").apply {
            color = ContextCompat.getColor(requireContext(), R.color.purple)
            setCircleColor(color)
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
        }

        // Update the chart with new data
        lineChart.data = LineData(dataSet)
        lineChart.invalidate()
        }
```

### Narrative
This artifact presents a great opportunity to demonstrate a variety of skills and concepts that are crucial to any form of development, but especially for mobile application development. Using this mobile app demonstrate important skills such as software development, data visualization, and general problem-solving. 

A mobile application can be constructed in a variety of ways, which is demonstrated by just how different my original approach is compared to this approach. Initially, the app used an in-memory database that lacked any sort of security features, poor life-cycle management, and a lot of bloated or redundant code. This attempt comes from a very different angle. Instead of needing a Model, Entity, ViewHolder, and Repository for every database object and process, I am able to dramatically streamline these workflows by tying these processes to a database repository, using a NavGraph, implementing a FragmentContainerView, and incorporating appropriate lifecycle management.  These component demonstrate significant growth in my understanding of app development, especially when it comes to general architecture and optimizing the performance of the application. Previously, I used activities for everything and didn’t lean into fragments enough. This enhancement utilizes fragments for the list of records, the settings screen, as well as the visual analytics screen, all of which are tied to the FragmentContainerView in the Main Activity file. Additionally, the visual analytics screen itself demonstrates the ability to utilize handle fragment lifecycles and create a user interface that provides more value to the user by visualizing their data. The Navigation Graph demonstrate modular designs by ensuring the weight records screen and analytics tab are both kept cleanly separated, but remain connected for any necessary use between the two. This also optimizes the general performance of the application. When a fragment is destroyed or created when using a NavGraph, the app’s resources are not unnecessarily consumed.

This specific enhancement involved optimizing the app’s performance, especially through the use of the NavGraph, FragmentContainerView, and properly manage the app’s resources. To further provide value to the user, I incorporated a sorting algorithm that will automatically display the user’s weight records is descending order based on their date. Additionally, when it comes to the visual analytics, I had to figure out a way to present the data in a visually appealing way. Initially, the graph started at zero for the weight and only went as high as the highest weight record. This resulted in a graph that only displayed the line portion of it in the top 20% of the graph. To resolve this, I utilized minOf and maxOf to calculate the y-axis range. 

	MPAndroidChart presents the industry-standard way to visualize data for a mobile application. Integrating it shows how I am able to use third-party tools effectively and understand how to properly integrate them into a project’s codebase. I also used Android’s Jetpack Navigation library for creating and using the NavGraph and Bottom Navigation. When these are tied into Firebase’s Realtime Database, the app offers a responsive and real-time solution for the user’s visual analytics for their data. 
 
	Equally important is the course outcome of developing a security mindset. This is where the lifecycle and resource management comes into play, as the use of the NavGraph and FragmentContainerView somewhat enforces the developer to address memory leaks – an issue I ran into during the development and testing of this app. It’s crucial to properly manage the app’s resources to prevent these security risks, such as the memory leak, and ensuring any listeners are properly destroyed when appropriate to do so. I incorporated several null checks for when the chart is created and updated to ensure those edge cases are accounted for and any sort of crash would be prevented. 
 
	 The process of incorporating this enhancement introduced a lot of new processes and concepts to me. I previously did not have any experience with utilizing third-party tools or libraries, such as MPAndroid. Additionally, I learned a lot of about lifecycle management in mobile development, how to properly utilize Firebase listeners, and use MPAndroidChart to create a visual graph of data stored by the app. One of the major challenges I ran into was first figuring out how to utilize Android Jetpack and MPAndroidChart, which involved me figuring out how to properly update the Gradle build and libraries.toml file. Secondly, after I incorporated the visual analytics screen, I began testing it and noticed that if I checked the Analytics screen, then went back to add or update a weight record, then tried to submit the new record, the app would crash. I was able to add a record without issue, but the problem stemmed from checking the Analytics tab, then trying to submit a change. After some thorough debugging, I found that the issue stemmed from one of the weight record listeners persisting after the Analytics fragment was destroyed. This caused the app to try and register the change with the graph, but would encounter a problem as the fragment was already destroyed. There were other issues as well, such as needing to refactor the Main Activity the bottom sheet fragments after deciding to use a Bottom Navigation bar that utilized the NavGraph and FragmentContainerView. 

### Incorporating Feedback
Some of the feedback my instructor provided regarding this criteria includes:
- Could implement more robust error recovery mechanisms
- Could benefit from more extensive comments explaining the data structure choices

These recommendations were addressed when I implemented the comments seen in the above code and in the source code, as well as in the ways in which the application will catch and provide any errors that take place. These errors are provided to the user in the form of a Toast text. 
 
## Database {#database}
This category intended to focus on adding adding more complex data concepts, data mining, or migrating from one form of a database to another. My approach to resolving this category included: 
- Migrating from Room to Firebase Realtime Database and Firebase Auth
- Implementing a cloud-based solution for data persistence across any devices
- Supporting offline data caching.
- Secure user login and authentication experience

### Code Example
```json
{
  users: {
    user_id: 1234567890abcdefg
      email: "test@testing.com"
      target_weight: 175
      uid: "1234567890abcdefg"
      weight_records: {
        weight_id: "1234567890abcdefg" {
          date: 1733090423203
          id: "1234567890abcdefg"
          weight: 182
        }
      }
  }
}
```
Another example can be seen in the database functions:

```kotlin
 // == Weight Record Methods == //
    fun addWeightRecord(weight: Double, date: Long, onSuccess:() -> Unit, onError: (String) -> Unit) {
        // If the user is not logged in or authenticated, return early
        val currentUser = auth.currentUser?.uid ?: return
        val recordRef = usersRef.child(currentUser).child("weight_records").push()
        val weightRecord = WeightRecord(id = recordRef.key ?: "", weight = weight, date = date, userId = currentUser)
        recordRef.setValue(weightRecord)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {onError(it.message ?: "Error adding weight record")}
    }

    // == Updating Weight Record == //
    fun updateWeightRecord(record: WeightRecord, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // If the user is not logged in or authenticated, return early
        val currentUser = auth.currentUser?.uid ?: return
        usersRef.child(currentUser).child("weight_records").child(record.id)
            .setValue(record)
            .addOnSuccessListener{ onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error updating weight record")}
    }

    // == Deletion of Weight Record == //
    fun deleteWeightRecord(record: WeightRecord, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // If the user is not logged in or authenticated, return early
        val currentUser =  auth.currentUser?.uid?: return
        usersRef.child(currentUser).child("weight_records").child(record.id)
            .removeValue()
            .addOnSuccessListener{ onSuccess()}
            .addOnFailureListener { onError(it.message ?: "Error removing weight record")}
    }

    // == Add or change Target Weight == //
    fun updateTargetWeight(targetWeight: Double, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // If the uer is not logged in or authenticated, return early
        val currentUser = auth.currentUser?.uid ?: return
        usersRef.child(currentUser).child("targetWeight").setValue(targetWeight)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Error updating target weight")
            }
    }
```

### Narrative
The weight tracking application provided a great opportunity to demonstrate a variety of skills, showcase my ability to enhance and improve an existing project, and dive into modernized approaches in software development. Specifically, this artifact and the enhancements I’ve made so far demonstrate my ability to effectively create mobile apps that support software design and engineering principles, as well as migrating and managing a database. Together, they represent a well-thought out mobile application that can effectively scale as needed and be distributed across a variety of devices without losing the appeal of real-time database functionality. 

	As mentioned in the previous narrative, the enhancements implemented address the remaining tasks for the Software Design and Engineering and Database criteria. These enhancements allowed me to target specific components of the application I wanted to improve, such as the back-end database, login activity, database repository, and the RecyclerView and adapter itself. An example of how I showcased my skills and abilities in Software Design and Engineering can be seen primarily in the process of refactoring the code from Java to Kotlin. Kotlin is widely accepted as the industry standard in mobile application development, handles UI processes much more effectively than Java does through the use of Coroutines and such, and allows a developer to tap into techniques such as Lambda functions to handle many aspects of the app’s functionality. This refactoring allowed me to implement a clean separation of concerns and segmenting specific parts of the code out to their own, as well as utilizing UI and UX techniques through the use of bottom sheet fragments, the recycler view and adapter, view holders, and view binding to implement the layouts created in XML files pertaining to each activity or fragment. 
 
	The rest of the Database enhancement has also been implemented through the creation and implementation of Firebase Realtime Database. Firebase provides an excellent way to incorporate real-time functionality across any devices a user is using the application with. Instead of relying on an in-memory database, as done in the initial version of the application, Firebase utilizes a cloud-based database that can effectively scale and in much more flexible. Instead of relying on a rigid and traditional database, Firebase simply adapts to the data and objects that are passed to it and creates nodes as needed in a JSON format. This provides an excellent resource for the application, as it automatically handles several processes that were difficult to address in the original version of the app that used Room for a database of the user information and weight records of theirs. This cloud database will automatically create the root node that has children nodes for each user ID. These user IDs are created and handled through Firebase Auth, further streamlining the user creation and managing of data. Each user ID node has several children nodes that make up their data. These nodes include their email, target weight, and a list of weight records. The weight records list acts as a parent node for each individual weight record, of which contain children nodes for “date”, “id”, “userId”, and “weight”. This tree of nodes that make up the general structure of the database dramatically streamlines the entire database interactions process. Previously, the original application relied on three separate tables, did not offer a scalable data storage solution, did not support security-oriented development, and would not be available on any other devices the user owns. 
 
	To implement the Firebase Database, I created a DatabaseRepository.kt file that handles the CRUD functionality necessary for the app’s logic. Any database operations are also validated through functions that ensure the user cannot pick future dates, enters valid weights, and that the objects are observed correctly. This required created Observers for the target weight and the weight records. Creating the database functionality, especially in relation to how a date is stored, required the use of milliseconds as the way of ingesting, transforming, and storing the date fields of a weight record. This method allows each record to be stored and compared efficiently, such as automatically sorting the weight records based on date – something that could not be done in the previous version of the app due to the way in which is stored the Date as a String. 

### Incorporating Feedback
Some of the feedback provided by my instructor included:
- Offline data caching
- Data validation

Firebase Realtime Database inherently supports offline data caching, allowing the app to automatically update the cloud-based database with the user's local database in the event they making any changes while disconnected from the internet. Additionally, I created and incorporate a data checker as a form of validation in addition to ensuring any user input for the weight was a valid entry. 



        

  
  
