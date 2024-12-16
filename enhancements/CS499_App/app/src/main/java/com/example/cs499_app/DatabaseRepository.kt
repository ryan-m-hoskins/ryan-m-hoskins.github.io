package com.example.cs499_app

import WeightRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class DatabaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val usersRef = database.getReference("users")
    private val weightRecordsRef = database.getReference("weight_records")
    private val targetWeightRef = database.getReference("target_weight")

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


    // == Retrieve the target weight from the database == //
    fun getTargetWeight(onSuccess: (Double?) -> Unit, onError: (String) -> Unit) {
        // If the user is logged in and authenticated, continue; otherwise, return early
        val currentUser = auth.currentUser?.uid ?: return
        // Reference to the target weight node in the database
        usersRef.child(currentUser).child("targetWeight")
            // get the target weight
            .get()
            .addOnSuccessListener { snapshot ->
                val targetWeight = snapshot.getValue(Double::class.java)
                onSuccess(targetWeight)
            }
            // If there is an error, notify user with error message
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Failed to fetch target weight")
            }
    }

    // == Method For Real-Time Observer of Target Weight and Weight Records == //
    private var targetWeightListener: ValueEventListener? = null
    private var weightRecordListener: ValueEventListener? = null

    fun observeTargetWeight(onUpdate: (Double?) -> Unit, onError: (String) -> Unit) {
        val currentUser = auth.currentUser?.uid ?: return
        // Remove any existing listeners before adding a new one
        removeTargetWeightListener()

        // Create new listener for any changes to target weight
        targetWeightListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val targetWeight = snapshot.getValue(Double::class.java)
                onUpdate(targetWeight)
            }
            // Handles error if one occurs
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        }
        // Reference targetWeight node in database and set up listener
        usersRef.child(currentUser).child("targetWeight")
            .addValueEventListener(targetWeightListener!!)
    }

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
    // Cleanup of listener for target weight
    fun removeTargetWeightListener() {
        val currentUser = auth.currentUser?.uid ?: return
        targetWeightListener?.let { listener ->
            usersRef.child(currentUser).child("targetWeight").removeEventListener(listener)
        }
        targetWeightListener = null
    }

    // == Method to validate user's input for date
    fun checkDateExists(date: Long, onResult: (Boolean) -> Unit, onError: (String) -> Unit) {
        val currentUser = auth.currentUser?.uid ?: return
        // Convert input date to the beginning of the day
        val startOfDay = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        // Convert to end of the day
        val endOfDay = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        // Navigate to current user node, weight records, order by date
        usersRef.child(currentUser).child("weight_records").orderByChild("date")
            // Start with beginning until the end of the day
            .startAt(startOfDay.toDouble()).endAt(endOfDay.toDouble())
            .get()
            // Return true if such a record exists
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.exists())
            }
            // Standard error handling if one occurs
            .addOnFailureListener {
                onError(it.message ?: "Error checking date")
            }
    }
}