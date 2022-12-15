package com.potato.timetable.colleges.base

import com.potato.oneall.colleges.AIITCollege
import com.potato.oneall.colleges.base.CollegeFactory
import junit.framework.TestCase

class CollegeFactoryTest : TestCase() {

    fun testGetCollegeNameList() {
        println(CollegeFactory.collegeNameList)
    }

    fun testCreateCollege() {
        assertNull(CollegeFactory.createCollege(null))
        assertNull(CollegeFactory.createCollege(""))
        assertTrue(CollegeFactory.createCollege(AIITCollege.NAME) is AIITCollege)
    }
}