package lowentry.ue4.libs.jackson.dataformat.csv;

import lowentry.ue4.libs.jackson.core.TSFBuilder;

/**
 * {@link lowentry.ue4.libs.jackson.core.TSFBuilder}
 * implementation for constructing {@link CsvFactory}
 * instances.
 *
 * @since 3.0
 */
@SuppressWarnings("all")
public class CsvFactoryBuilder extends TSFBuilder<CsvFactory, CsvFactoryBuilder>
{
    /*
    /**********************************************************
    /* Configuration
    /**********************************************************
     */

    /**
     * Set of {@link CsvParser.Feature}s enabled, as bitmask.
     */
    protected int _formatParserFeatures;

    /**
     * Set of {@link CsvGenerator.Feature}s enabled, as bitmask.
     */
    protected int _formatGeneratorFeatures;

    /*
    /**********************************************************
    /* Life cycle
    /**********************************************************
     */

    protected CsvFactoryBuilder() {
        _formatParserFeatures = CsvFactory.DEFAULT_CSV_PARSER_FEATURE_FLAGS;
        _formatGeneratorFeatures = CsvFactory.DEFAULT_CSV_GENERATOR_FEATURE_FLAGS;
    }

    public CsvFactoryBuilder(CsvFactory base) {
        super(base);
        _formatParserFeatures = base._csvParserFeatures;
        _formatGeneratorFeatures = base._csvGeneratorFeatures;
    }

    // // // Parser features

    public CsvFactoryBuilder enable(CsvParser.Feature f) {
        _formatParserFeatures |= f.getMask();
        return _this();
    }

    public CsvFactoryBuilder enable(CsvParser.Feature first, CsvParser.Feature... other) {
        _formatParserFeatures |= first.getMask();
        for (CsvParser.Feature f : other) {
            _formatParserFeatures |= f.getMask();
        }
        return _this();
    }

    public CsvFactoryBuilder disable(CsvParser.Feature f) {
        _formatParserFeatures &= ~f.getMask();
        return _this();
    }

    public CsvFactoryBuilder disable(CsvParser.Feature first, CsvParser.Feature... other) {
        _formatParserFeatures &= ~first.getMask();
        for (CsvParser.Feature f : other) {
            _formatParserFeatures &= ~f.getMask();
        }
        return _this();
    }

    public CsvFactoryBuilder configure(CsvParser.Feature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Generator features

    public CsvFactoryBuilder enable(CsvGenerator.Feature f) {
        _formatGeneratorFeatures |= f.getMask();
        return _this();
    }

    public CsvFactoryBuilder enable(CsvGenerator.Feature first, CsvGenerator.Feature... other) {
        _formatGeneratorFeatures |= first.getMask();
        for (CsvGenerator.Feature f : other) {
            _formatGeneratorFeatures |= f.getMask();
        }
        return _this();
    }

    public CsvFactoryBuilder disable(CsvGenerator.Feature f) {
        _formatGeneratorFeatures &= ~f.getMask();
        return _this();
    }
    
    public CsvFactoryBuilder disable(CsvGenerator.Feature first, CsvGenerator.Feature... other) {
        _formatGeneratorFeatures &= ~first.getMask();
        for (CsvGenerator.Feature f : other) {
            _formatGeneratorFeatures &= ~f.getMask();
        }
        return _this();
    }

    public CsvFactoryBuilder configure(CsvGenerator.Feature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Accessors

    public int formatParserFeaturesMask() { return _formatParserFeatures; }
    public int formatGeneratorFeaturesMask() { return _formatGeneratorFeatures; }

    /**
     * @return Factory instance configured with builder configuration
     */
    @Override
    public CsvFactory build() {
        // 28-Dec-2017, tatu: No special settings beyond base class ones, so:
        return new CsvFactory(this);
    }
}
