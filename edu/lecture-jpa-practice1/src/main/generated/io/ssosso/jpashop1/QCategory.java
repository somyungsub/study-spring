package io.ssosso.jpashop1;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -2011994810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCategory category = new QCategory("category");

    public final ListPath<Category, QCategory> child = this.<Category, QCategory>createList("child", Category.class, QCategory.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<io.ssosso.jpashop1.domain.item.Item, io.ssosso.jpashop1.domain.item.QItem> items = this.<io.ssosso.jpashop1.domain.item.Item, io.ssosso.jpashop1.domain.item.QItem>createList("items", io.ssosso.jpashop1.domain.item.Item.class, io.ssosso.jpashop1.domain.item.QItem.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final QCategory parent;

    public QCategory(String variable) {
        this(Category.class, forVariable(variable), INITS);
    }

    public QCategory(Path<? extends Category> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCategory(PathMetadata metadata, PathInits inits) {
        this(Category.class, metadata, inits);
    }

    public QCategory(Class<? extends Category> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

