package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.policy.TestEntity
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.dsl.ModelFilterBuilder
import studio.crud.crudframework.util.filtersMatch
import java.util.Optional
import java.util.Spliterator
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.BinaryOperator
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.IntFunction
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.function.ToDoubleFunction
import java.util.function.ToIntFunction
import java.util.function.ToLongFunction
import java.util.stream.Collector
import java.util.stream.DoubleStream
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.streams.toList

fun main() {
    val crudHandler = NewCrudHandlerImpl()
    val test = crudHandler.search<TestEntity>(DynamicModelFilter {

    })
                    .persistCopy()
                    .skip(5)
                    .limit(3)
                    .map { it.name }
                    .toList()

}



class NewCrudHandlerImpl {
    val sampleDao = SampleDao()
    // todo: pass class somehow
    fun <T : BaseCrudEntity<*>> search(filter: DynamicModelFilter) : BuilderOrStream<T> {
        return BuilderOrStream { ctx ->
            sampleDao.search(filter) as Stream<T>
        }
    }

    fun <T : BaseCrudEntity<*>> search(block: ModelFilterBuilder<T>.() -> Unit) : BuilderOrStream<T> {
        val filterFieldsBuilder = ModelFilterBuilder<T>()
        filterFieldsBuilder.block()
        return search(filterFieldsBuilder.build())
    }
}



class BuilderOrStreamContext<EntityType : BaseCrudEntity<*>>(val persistCopy: Boolean)

class BuilderOrStream<EntityType : BaseCrudEntity<*>>(val streamSupplier: (BuilderOrStreamContext<EntityType>) -> Stream<EntityType>) : Stream<EntityType> {
    private val stream by lazy { streamSupplier(BuilderOrStreamContext(persistCopy)) }
    var persistCopy = false
    fun persistCopy(): BuilderOrStream<EntityType> {
        persistCopy = true
        return this
    }

    override fun close() {
        stream.close()
    }

    override fun iterator(): MutableIterator<EntityType> {
        return stream.iterator()
    }

    override fun spliterator(): Spliterator<EntityType> {
        return stream.spliterator()
    }

    override fun isParallel(): Boolean {
        return stream.isParallel
    }

    override fun sequential(): Stream<EntityType> {
        return stream.sequential()
    }

    override fun parallel(): Stream<EntityType> {
        return stream.parallel()
    }

    override fun unordered(): Stream<EntityType> {
        return stream.unordered()
    }

    override fun onClose(closeHandler: Runnable?): Stream<EntityType> {
        return stream.onClose(closeHandler)
    }

    override fun distinct(): Stream<EntityType> {
        return stream.distinct()
    }

    override fun sorted(): Stream<EntityType> {
        return stream.sorted()
    }

    override fun limit(maxSize: Long): Stream<EntityType> {
        return stream.limit(maxSize)
    }

    override fun skip(n: Long): Stream<EntityType> {
        return stream.skip(n)
    }

    override fun toArray(): Array<Any> {
        return stream.toArray()
    }

    override fun <A : Any?> toArray(generator: IntFunction<Array<A>>?): Array<A> {
        return stream.toArray(generator)
    }

    override fun count(): Long {
        return stream.count()
    }

    override fun findFirst(): Optional<EntityType> {
        return stream.findFirst()
    }

    override fun findAny(): Optional<EntityType> {
        return stream.findAny()
    }

    override fun noneMatch(predicate: Predicate<in EntityType>?): Boolean {
        return stream.noneMatch(predicate)
    }

    override fun allMatch(predicate: Predicate<in EntityType>?): Boolean {
        return stream.allMatch(predicate)
    }

    override fun anyMatch(predicate: Predicate<in EntityType>?): Boolean {
        return stream.anyMatch(predicate)
    }

    override fun max(comparator: Comparator<in EntityType>?): Optional<EntityType> {
        return stream.max(comparator)
    }

    override fun min(comparator: Comparator<in EntityType>?): Optional<EntityType> {
        return stream.min(comparator)
    }

    override fun <R : Any?, A : Any?> collect(collector: Collector<in EntityType, A, R>?): R {
        return stream.collect(collector)
    }

    override fun <R : Any?> collect(supplier: Supplier<R>?, accumulator: BiConsumer<R, in EntityType>?, combiner: BiConsumer<R, R>?): R {
        return stream.collect(supplier, accumulator, combiner)
    }

    override fun <U : Any?> reduce(identity: U, accumulator: BiFunction<U, in EntityType, U>?, combiner: BinaryOperator<U>?): U {
        return stream.reduce(identity, accumulator, combiner)
    }

    override fun reduce(accumulator: BinaryOperator<EntityType>?): Optional<EntityType> {
        return stream.reduce(accumulator)
    }

    override fun reduce(identity: EntityType, accumulator: BinaryOperator<EntityType>?): EntityType {
        return stream.reduce(identity, accumulator)
    }

    override fun forEachOrdered(action: Consumer<in EntityType>?) {
        stream.forEachOrdered(action)
    }

    override fun forEach(action: Consumer<in EntityType>?) {
        stream.forEach(action)
    }

    override fun peek(action: Consumer<in EntityType>?): Stream<EntityType> {
        return stream.peek(action)
    }

    override fun sorted(comparator: Comparator<in EntityType>?): Stream<EntityType> {
        return stream.sorted(comparator)
    }

    override fun flatMapToDouble(mapper: Function<in EntityType, out DoubleStream>?): DoubleStream {
        return stream.flatMapToDouble(mapper)
    }

    override fun flatMapToLong(mapper: Function<in EntityType, out LongStream>?): LongStream {
        return stream.flatMapToLong(mapper)
    }

    override fun flatMapToInt(mapper: Function<in EntityType, out IntStream>?): IntStream {
        return stream.flatMapToInt(mapper)
    }

    override fun <R : Any?> flatMap(mapper: Function<in EntityType, out Stream<out R>>?): Stream<R> {
        return stream.flatMap(mapper)
    }

    override fun mapToDouble(mapper: ToDoubleFunction<in EntityType>?): DoubleStream {
        return stream.mapToDouble(mapper)
    }

    override fun mapToLong(mapper: ToLongFunction<in EntityType>?): LongStream {
        return stream.mapToLong(mapper)
    }

    override fun mapToInt(mapper: ToIntFunction<in EntityType>?): IntStream {
        return stream.mapToInt(mapper)
    }

    override fun <R : Any?> map(mapper: Function<in EntityType, out R>?): Stream<R> {
        return stream.map(mapper)
    }

    override fun filter(predicate: Predicate<in EntityType>?): Stream<EntityType> {
        return stream.filter(predicate)
    }
}

class SampleDao {
    val entities = mutableListOf(
        TestEntity(0L, "test1"),
        TestEntity(1L, "test2"),
        TestEntity(2L, "test3"),
        TestEntity(3L, "test1"),
        TestEntity(4L, "test5"),
        TestEntity(5L, "test6"),
        TestEntity(6L, "test7"),
        TestEntity(7L, "test1"),
        TestEntity(8L, "test9"),
        TestEntity(9L, "test1"),
    )

    fun search(filter: DynamicModelFilter): Stream<TestEntity> {
        val stream = StreamSupport.stream(entities.spliterator(), false)
                .peek { println("accessed ${it.id}") }
                .filter { filter.filtersMatch(it) }
        if (filter.start != null) {
            stream.skip(filter.start?.toLong()!!)
        }
        if (filter.limit != null) {
            stream.limit(filter.limit?.toLong()!!)
        }
        return stream
    }
}