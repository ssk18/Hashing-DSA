package org.example

class DoubleHashing(private val capacity:Int) {
    private val hashArray = IntArray(capacity) { -1 } // Stores the keys, -1 denotes empty
    private val countArray = IntArray(capacity) { 0 } // Stores the count values
    private val stateArray = CharArray(capacity) { 'e' } // 'o' for occupied, 'e' for empty, 'd' for deleted

    // First hash function (primary hash)
    private fun h1(key: Int): Int {
        return key % capacity
    }

    // Second hash function (step size for double hashing)
    private fun h2(key: Int): Int {
        return 1 + (key % (capacity - 1)) // Ensure step size is non-zero
    }

    // Hash function with double hashing (key + i * h2(key)) % capacity
    private fun hash(key: Int, i: Int): Int {
        return (h1(key) + i * h2(key)) % capacity
    }

    // Insert a value into the hash table using double hashing
    fun insert(key: Int, count: Int) {
        var i = 0
        var index = hash(key, i)

        // Double hashing to find the next available slot
        while (stateArray[index] == 'o' && hashArray[index] != key) {
            i++
            index = hash(key, i) // Probing to the next slot using double hashing
        }

        // Insert the key and count
        hashArray[index] = key
        countArray[index] = count
        stateArray[index] = 'o' // Mark the slot as occupied
    }

    // Search for a value in the hash table using double hashing
    fun search(key: Int): Int? {
        var i = 0
        var index = hash(key, i)
        val startIndex = index // To detect a full loop (when the table is full)

        // Double hashing to find the key
        while (stateArray[index] != 'e') {
            if (stateArray[index] == 'o' && hashArray[index] == key) {
                return countArray[index] // Return the associated count if found
            }
            i++
            index = hash(key, i)
            if (index == startIndex) break // We have probed the whole table
        }
        return null // Key not found
    }

    // Remove a value from the hash table and shift subsequent elements
    fun remove(key: Int): Boolean {
        var i = 0
        var index = hash(key, i)
        val startIndex = index

        // Double hashing to find the key
        while (stateArray[index] != 'e') {
            if (stateArray[index] == 'o' && hashArray[index] == key) {
                // Key found, mark the current slot as empty
                stateArray[index] = 'e'
                hashArray[index] = -1
                countArray[index] = 0

                // Shift subsequent elements back to fill the gap
                shiftElementsBack(index)
                return true
            }
            i++
            index = hash(key, i)
            if (index == startIndex) break
        }
        return false // Key not found
    }

    // Shift elements back after deletion to maintain probe sequence integrity
    private fun shiftElementsBack(deletedIndex: Int) {
        var index = (deletedIndex + 1) % capacity
        var deletedKeyIndex = deletedIndex

        while (stateArray[index] == 'o') {
            val rehashIndex = hash(hashArray[index], 0) // Get the original hash of the key

            // Check if the element should be moved back to fill the deleted slot
            if (rehashIndex <= deletedIndex || rehashIndex > index) {
                // Move the current element back to the deleted slot
                hashArray[deletedKeyIndex] = hashArray[index]
                countArray[deletedKeyIndex] = countArray[index]
                stateArray[deletedKeyIndex] = 'o'

                // Mark the current slot as empty
                stateArray[index] = 'e'
                hashArray[index] = -1
                countArray[index] = 0

                // Now treat the current index as the new "deleted" slot
                deletedKeyIndex = index
            }

            // Move to the next index in the probing sequence
            index = (index + 1) % capacity
        }
    }
}