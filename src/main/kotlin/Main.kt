package org.example

fun main() {
    println(generateSubarraySum(intArrayOf(5, 8, -15, 2, 7, -3, 5, 4, 9, -35, 8, 2, 4)))
    println(longestContinuousLength1(intArrayOf(9, 2, 7, 28, 6, 23, 22, 23)))
    println(
        findWinners1(
            arrayOf(
                intArrayOf(1, 3),
                intArrayOf(2, 3),
                intArrayOf(3, 6),
                intArrayOf(5, 6),
                intArrayOf(5, 7),
                intArrayOf(4, 5),
                intArrayOf(4, 8),
                intArrayOf(4, 9),
                intArrayOf(10, 4),
                intArrayOf(10, 9)
            )
        )
    )
    println(
        equalPairsOptimized(
            arrayOf(
                intArrayOf(3, 1, 2, 2),
                intArrayOf(1, 4, 4, 5),
                intArrayOf(2, 4, 2, 2),
                intArrayOf(2, 4, 2, 2)
            )
        )
    )
    println(numIdenticalPairs(intArrayOf(1, 1, 1, 1)))
    println(restoreArray(arrayOf(intArrayOf(2, 1), intArrayOf(3, 4), intArrayOf(3, 2))).contentToString())
    println(countNicePairs(intArrayOf(13, 10, 35, 24, 76)))
    println(canArrange(arr = intArrayOf(1,2,3,4,5,6), k = 7))
}

/*
Possible Subarrays: n*(n+1) / 2

possible subsets: 2 power n. can not be applied to an array with duplicate elements

possible subsequences: similar to subsets but elements order should be maintained.
2,10,5,18,-4 -> 2,5,18 but 5,2,18 not valid
*/


// Maximum possible subarray sum : n2 and 1
fun generateSubarraySum(arr: IntArray): Int {
    var ans = Int.MIN_VALUE
    for (start in arr.indices) {
        var sum = 0
        for (end in start until arr.size) {
            sum += arr[end]
            ans = maxOf(ans, sum)
        }
    }
    return ans
}

// length of the longest subarray whose elements can be arranged in continuous manner
fun longestContinuousLength(arr: IntArray): Int {
    var ans = 0
    for (i in 0 until arr.size - 1) {
        val hashSet = HashSet<Int>()
        var min = arr[i]
        var max = arr[i]
        hashSet.add(arr[i])
        for (j in i + 1 until arr.size) {
            if (hashSet.contains(arr[j])) break
            hashSet.add(arr[j])
            min = minOf(min, arr[j])
            max = maxOf(max, arr[j])

            if (max - min >= arr.size) break

            if (max - min == j - i) {
                val length = j - i + 1
                ans = maxOf(ans, length)
            }
        }
    }
    return ans
}

// another way to prevent a iterations if max - min >= arr.size
fun longestContinuousLength1(arr: IntArray): Int {
    var ans = 0
    var i = 0
    while (i < arr.size - 1) {
        val hashSet = HashSet<Int>()
        var min = arr[i]
        var max = arr[i]
        hashSet.add(arr[i])
        for (j in i + 1 until arr.size) {
            if (hashSet.contains(arr[j])) break
            hashSet.add(arr[j])
            min = minOf(min, arr[j])
            max = maxOf(max, arr[j])

            // If max - min >= arr.size, skip to j + 1
            if (max - min >= arr.size) {
                i = j + 1 // Set i to j to skip ahead
                break
            }

            if (max - min == j - i) {
                val length = j - i + 1
                ans = maxOf(ans, length)
            }
        }
        i++
    }
    return ans
}

// https://leetcode.com/problems/find-players-with-zero-or-one-losses/
fun findWinners(matches: Array<IntArray>): List<List<Int>> {
    val lostHashMap = HashMap<Int, Int>()
    for (i in matches.indices) {
        val loser = matches[i][1]
        val winner = matches[i][0]
        lostHashMap.putIfAbsent(winner, 0)
        lostHashMap[loser] = lostHashMap.getOrDefault(matches[i][1], 0) + 1
    }
    val winnerList = mutableListOf<Int>()
    val loserList = mutableListOf<Int>()
    for ((key, value) in lostHashMap) {
        if (value == 0) {
            winnerList.add(key)
        } else if (value == 1) {
            loserList.add(key)
        }
    }
    return listOf(winnerList.sorted(), loserList.sorted())
}

fun findWinners1(matches: Array<IntArray>): List<List<Int>> {
    var answer = mutableListOf<MutableList<Int>>()
    var lossCount = mutableMapOf<Int, Int>()

    for (game in matches) {
        lossCount.putIfAbsent(game[0], 0)

        // Increment the loser's loss count by 1
        lossCount[game[1]] = lossCount.getOrDefault(game[1], 0) + 1
    }

    val noLossArray = mutableListOf<Int>()
    val oneLossArray = mutableListOf<Int>()

    for ((key, value) in lossCount) {
        if (value == 0) {
            noLossArray.add(key)
        } else if (value == 1) {
            oneLossArray.add(key)
        }
    }

    noLossArray.sort()
    oneLossArray.sort()

    answer.add(noLossArray)
    answer.add(oneLossArray)

    return answer
}

// https://leetcode.com/problems/unique-number-of-occurrences/description/
fun uniqueOccurrences(arr: IntArray): Boolean {
    val freqMap = HashMap<Int, Int>()

    for (i in arr.indices) {
        freqMap[arr[i]] = freqMap.getOrDefault(arr[i], 0) + 1
    }
    val freqSet = HashSet<Int>()
    for (entry in freqMap.entries) {
        if (!freqSet.add(entry.value)) {
            return false
        }
    }

    return true
}

// https://leetcode.com/problems/word-pattern/description/
fun wordPattern(pattern: String, s: String): Boolean {
    val patternMap = HashMap<Char, String>()
    val sMap = HashMap<String, Char>()
    val words = s.split(" ")

    if (pattern.length != words.size) return false

    for (i in pattern.indices) {
        val char = pattern[i]
        val word = words[i]

        if (patternMap.containsKey(char)) {
            if (patternMap[char] != word) {
                return false
            }
        } else {
            patternMap[char] = word
        }

        if (sMap.containsKey(word)) {
            if (sMap[word] != char) {
                return false
            }
        } else {
            sMap[word] = char
        }
    }
    return true
}

// https://leetcode.com/problems/smallest-number-in-infinite-set/description/
class SmallestInfiniteSet() {
    private var currentSmallest = 1
    private val hashSet = HashSet<Int>()

    fun popSmallest(): Int {
        if (hashSet.isNotEmpty()) {
            val smallest = hashSet.min()
            hashSet.remove(smallest)
            return smallest
        }

        val smallest = currentSmallest
        currentSmallest++
        return smallest
    }

    fun addBack(num: Int) {
        if (!hashSet.contains(num) && currentSmallest > num) {
            hashSet.add(num)
        }
    }
}

// https://leetcode.com/problems/equal-row-and-column-pairs/description/
// brute force
fun equalPairs(grid: Array<IntArray>): Int {
    var count = 0
    for (i in 0 until grid.size) {
        for (j in 0 until grid.size) {
            var isEqual = true
            for (k in 0 until grid.size) {
                if (grid[i][k] != grid[k][j]) {
                    isEqual = false
                    break
                }
            }
            if (isEqual) {
                count++
            }
        }
    }
    return count
}

//optimized
fun equalPairsOptimized(grid: Array<IntArray>): Int {
    val hashMap = HashMap<List<Int>, Int>()
    for (i in grid.indices) {
        val row = grid[i].toList()
        hashMap[row] = hashMap.getOrDefault(row, 0) + 1
    }
    var count = 0
    for (i in grid.indices) {
        val column = mutableListOf<Int>()
        for (element in grid) {
            column.add(element[i])
        }
        if (hashMap.containsKey(column)) {
            hashMap[column]?.let {
                count += it
            }
        }
    }
    return count
}

// https://leetcode.com/problems/number-of-good-pairs/description/
fun numIdenticalPairsBruteForce(nums: IntArray): Int {
    var count = 0
    for (i in 0 until nums.size - 1) {
        for (j in i + 1 until nums.size) {
            if (nums[i] == nums[j] && i < j) {
                count++
            }
        }
    }
    return count
}

fun numIdenticalPairs(nums: IntArray): Int {
    val pairMap = HashMap<Int, Int>()
    var count = 0
    for (i in nums.indices) {
        pairMap[nums[i]]?.let {
            count += it
        }
        pairMap[nums[i]] = pairMap.getOrDefault(nums[i], 0) + 1
    }
    return count
}

// https://leetcode.com/problems/restore-the-array-from-adjacent-pairs/description/
fun restoreArray(adjacentPairs: Array<IntArray>): IntArray {
    val map = HashMap<Int, MutableList<Int>>()
    for (pair in adjacentPairs) {
        val a = pair[0]
        val b = pair[1]
        map.computeIfAbsent(a) { mutableListOf<Int>() }.add(b)
        map.computeIfAbsent(b) { mutableListOf<Int>() }.add(a)
    }
    val arr = IntArray(map.size)
    var startPoint = -1
    for (entry in map.entries) {
        if (entry.value.size == 1) {
            startPoint = entry.key
            break
        }
    }

    arr[0] = startPoint
    arr[1] = map[startPoint]!![0]

    for (i in 2 until arr.size) {
        val neighbors = map[arr[i - 1]]!!
        arr[i] = if (neighbors[0] == arr[i - 2]) neighbors[1] else neighbors[0]
    }
    return arr
}

// https://leetcode.com/problems/count-nice-pairs-in-an-array/description/
fun countNicePairs(nums: IntArray): Int {
    val MOD = 1000000007
    val diffCountMap = HashMap<Int, Int>()
    var pairs = 0
    for (i in nums.indices) {
        val diff = nums[i] - nums[i].reverseInt()
        pairs = (pairs + diffCountMap.getOrDefault(diff, 0)) % MOD
        diffCountMap[diff] = (diffCountMap.getOrDefault(diff, 0) + 1) % MOD
    }

    return pairs
}

fun Int.reverseInt(): Int {
    var num = this
    var reversed = 0
    while (num != 0) {
        val digit = num % 10
        reversed = reversed * 10 + digit
        num /= 10
    }
    return reversed
}

// https://leetcode.com/problems/check-if-array-pairs-are-divisible-by-k/description/
fun canArrange(arr: IntArray, k: Int): Boolean {
    val map = HashMap<Int, Int>(k)
    for (i in arr.indices) {
        val remainder = (arr[i] % k + k) % k
        map[remainder] = map.getOrDefault(remainder, 0) + 1
    }

    if (map.getOrDefault(0, 0) % 2 != 0) return false

    for (i in 1..k/2) {
        val counterHalf = k - i
        if (map.getOrDefault(i, 0) != map.getOrDefault(counterHalf, 0)) return false
    }
    return true
}