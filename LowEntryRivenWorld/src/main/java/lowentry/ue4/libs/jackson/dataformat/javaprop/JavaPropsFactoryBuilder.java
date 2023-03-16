package lowentry.ue4.libs.jackson.dataformat.javaprop;

import lowentry.ue4.libs.jackson.core.TSFBuilder;

/**
 * {@link TSFBuilder}
 * implementation for constructing {@link JavaPropsFactory}
 * instances.
 *
 * @since 3.0
 */
@SuppressWarnings("all")
public class JavaPropsFactoryBuilder extends TSFBuilder<JavaPropsFactory, JavaPropsFactoryBuilder>
{
    public JavaPropsFactoryBuilder() {
        super();
    }

    public JavaPropsFactoryBuilder(JavaPropsFactory base) {
        super(base);
    }

    @Override
    public JavaPropsFactory build() {
        // 28-Dec-2017, tatu: No special settings beyond base class ones, so:
        return new JavaPropsFactory(this);
    }
}
