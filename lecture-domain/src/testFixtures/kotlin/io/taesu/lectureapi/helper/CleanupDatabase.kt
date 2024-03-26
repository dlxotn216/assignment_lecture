package io.taesu.lectureapi.helper

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import jakarta.transaction.Transactional
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Component
class CleanupDatabase(
    private var jdbcTemplate: JdbcTemplate,
    @PersistenceContext
    private var entityManager: EntityManager,
) {

    @Transactional
    fun all() {
        val tables = entityManager.metamodel
            .entities
            .filter { it.javaType.getAnnotation(Table::class.java) != null }
            .map { it.javaType.getAnnotation(Table::class.java).name }

        tables.forEach { table -> jdbcTemplate.execute("TRUNCATE table $table") }
    }
}
