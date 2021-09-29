package com.datasoft.abs.data.source.local.db.dao.customer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datasoft.abs.data.source.local.db.entity.customer.*
import com.datasoft.abs.data.source.local.db.entity.customer.relation.*

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeneral(general: General): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonal(personal: Personal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNominee(nominee: Nominee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGuardian(guardian: Guardian)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFingerprint(fingerprint: Fingerprint)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDocument(document: Document)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRiskGrading(riskGrading: RiskGrading)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDocumentIdentification(documentIdentification: List<DocumentIdentification>)

    @Query("UPDATE customer_document_identification SET is_collected = :isCollected WHERE id = :id")
    fun updateDocumentIsCollected(isCollected: Boolean, id: Int)

    @Query("UPDATE customer_document_identification SET is_verified = :isVerified WHERE id = :id")
    fun updateDocumentIsVerified(isVerified: Boolean, id: Int)

    @Query("SELECT * FROM customer_general_info")
    fun getAll(): LiveData<List<General>>

    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneral(generalId: Int): General

    @Transaction
    @Query("SELECT * FROM customer_personal_info WHERE id = :generalId")
    fun getGeneralAndPersonal(generalId: Int): GeneralAndPersonal

    @Transaction
    @Query("SELECT * FROM customer_personal_info WHERE id = :personalId")
    fun getPersonalAndNominee(personalId: Int): PersonalAndNominee

    @Transaction
    @Query("SELECT * FROM customer_personal_info WHERE id = :personalId")
    fun getPersonalAndGuardian(personalId: Int): PersonalAndGuardian

    @Transaction
    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneralWithAddresses(generalId: Int): GeneralWithAddresses

    @Transaction
    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneralAndPhoto(generalId: Int): GeneralAndPhoto

    @Transaction
    @Query("SELECT * FROM customer_photo_info WHERE id = :generalId")
    fun getPhoto(generalId: Int): LiveData<Photo>

    @Transaction
    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneralAndFingerprint(generalId: Int): GeneralAndFingerprint

    @Transaction
    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneralWithDocuments(generalId: Int): GeneralWithDocuments

    @Transaction
    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneralAndRiskGrading(generalId: Int): GeneralAndRiskGrading

    @Transaction
    @Query("SELECT * FROM customer_general_info WHERE id = :generalId")
    fun getGeneralAndDocumentIdentification(generalId: Int): GeneralAndDocumentIdentification

    @Query("SELECT * FROM customer_address_info WHERE generalId =:generalId")
    fun getAddress(generalId: Int): LiveData<List<Address>>

    @Query("SELECT * FROM customer_document_info WHERE generalId =:generalId")
    fun getDocuments(generalId: Int): LiveData<List<Document>>

    @Query("SELECT * FROM customer_document_identification WHERE generalId =:generalId")
    fun getDocumentIdentifications(generalId: Int): LiveData<List<DocumentIdentification>>

    @Query("DELETE FROM customer_general_info WHERE id = :generalId")
    fun delete(generalId: Int)

    @Query("DELETE FROM customer_address_info WHERE id = :addressId")
    fun deleteAddress(addressId: Int)

    @Query("DELETE FROM customer_document_info WHERE id = :documentId")
    fun deleteDocument(documentId: Int)
}