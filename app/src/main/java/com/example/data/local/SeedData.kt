package com.example.data.local

import com.example.data.model.*

object SeedData {

    val categories = listOf(
        CategoryEntity("fantasy", "فانتازيا وخيال", "Fantasy", "AutoAwesome", "عوالم سحرية ومغامرات أسطورية تتجاوز حدود الواقع", 6),
        CategoryEntity("scifi", "خيال علمي", "Sci-Fi", "RocketLaunch", "مستقبل التكنولوجيا، السفر عبر الفضاء، والذكاء الاصطناعي", 4),
        CategoryEntity("romance", "رومانسي ودراما", "Romance", "Favorite", "قصص حب مؤثرة، مشاعر صادقة وعلاقات إنسانية عميقة", 5),
        CategoryEntity("mystery", "غموض وجريمة", "Mystery & Thriller", "Psychology", "ألغاز محيرة، تحقيقات بوليسية وتشويق يحبس الأنفاس", 4),
        CategoryEntity("horror", "رعب وإثارة", "Horror", "Bedtime", "حكايات مظلمة وخوارق تثير الرهبة والفضول", 3),
        CategoryEntity("historical", "تاريخي وملحمي", "Historical", "HistoryEdu", "روايات مستوحاة من التاريخ وحضارات الشرق والغرب", 3),
        CategoryEntity("adventure", "مغامرات وتشويق", "Adventure", "Explore", "رحلات ملحمية، أسرار دفينة وتحديات البقاء", 4),
        CategoryEntity("poetry", "شعر ونصوص", "Poetry & Prose", "FormatQuote", "خواطر أدبية ونصوص شعرية راقية", 3),
        CategoryEntity("comedy", "كوميديا وساخر", "Comedy", "Mood", "حكايات طريفة ومواقف فكاهية ممتعة", 2),
        CategoryEntity("young_adult", "يافعين ونشء", "Young Adult", "School", "قصص لاكتشاف الذات وتحديات مرحلة الشباب", 3)
    )

    val users = listOf(
        UserEntity(
            id = 1,
            username = "mina_alabd",
            displayName = "مينا العبد",
            email = "mina.alabd@gmail.com",
            bio = "مطور ومؤسس منصة قصة. كاتب شغوف بالخيال العلمي والأدب التفاعلي.",
            role = "ADMIN",
            followersCount = 1420,
            followingCount = 45,
            storiesCount = 3,
            readsReceived = 28400
        ),
        UserEntity(
            id = 2,
            username = "tariq_omran",
            displayName = "طارق عمران",
            email = "tariq@qessa.app",
            bio = "روائي وباحث تاريخي. مؤلف ملحمة رمال الشرق وواحة الأرواح.",
            role = "WRITER",
            followersCount = 3890,
            followingCount = 120,
            storiesCount = 2,
            readsReceived = 67500
        ),
        UserEntity(
            id = 3,
            username = "sara_nour",
            displayName = "سارة نور",
            email = "sara@qessa.app",
            bio = "شاعرة وكاتبة رومانسية معاصرة. أكتب نبضات القلوب ودفء المقاهي.",
            role = "WRITER",
            followersCount = 5210,
            followingCount = 89,
            storiesCount = 2,
            readsReceived = 94200
        ),
        UserEntity(
            id = 4,
            username = "ziad_cyber",
            displayName = "زياد المهندس",
            email = "ziad@qessa.app",
            bio = "مهندس ذكاء اصطناعي وروائي سايبربانك. أستكشف الغد الرقمي.",
            role = "WRITER",
            followersCount = 2150,
            followingCount = 64,
            storiesCount = 2,
            readsReceived = 38900
        ),
        UserEntity(
            id = 5,
            username = "layla_nasser",
            displayName = "ليلى ناصر",
            email = "layla@qessa.app",
            bio = "كاتبة ألغاز وتشويق بوليسي. لا شيء كما يبدو في الظاهر!",
            role = "WRITER",
            followersCount = 4300,
            followingCount = 110,
            storiesCount = 2,
            readsReceived = 81200
        ),
        UserEntity(
            id = 6,
            username = "mod_hassan",
            displayName = "حسن المعتصم",
            email = "mod@qessa.app",
            bio = "مشرف مجتمع قصة. حريص على بيئة قراءة نقية ومحترمة للجميع.",
            role = "MODERATOR",
            followersCount = 850,
            followingCount = 30,
            storiesCount = 0,
            readsReceived = 0
        ),
        UserEntity(
            id = 7,
            username = "reader_ahmed",
            displayName = "أحمد خالد",
            email = "ahmed@reader.com",
            bio = "قارئ نهم يعيش بين صفحات الكتب والروايات العالمية.",
            role = "READER",
            followersCount = 340,
            followingCount = 280,
            storiesCount = 0,
            readsReceived = 0
        ),
        UserEntity(
            id = 8,
            username = "maram_arts",
            displayName = "مرام الشريف",
            email = "maram@qessa.app",
            bio = "فنانة وكاتبة أدب يافعين وفانتازيا شرقية.",
            role = "WRITER",
            followersCount = 1890,
            followingCount = 95,
            storiesCount = 1,
            readsReceived = 19200
        )
    )

    val stories = listOf(
        StoryEntity(
            id = 1,
            authorId = 2,
            authorName = "طارق عمران",
            title = "أسرار رمال النجوم",
            description = "في عمق صحراء النقب الخفية، يعثر رحّالة شاب على مخطوطة فلكية منسية تكشف مسار بوابة عبر النجوم. ملحمة فانتازيا تجمع بين الأساطير الشرقية وألغاز الفلك القديم.",
            coverResName = "cover_desert_stars",
            categoryId = "fantasy",
            tags = "فانتازيا, صحراء, سحر, أساطير, نجوم, مغامرة",
            language = "ar",
            ageRating = "ALL",
            status = "ONGOING",
            readsCount = 45200,
            likesCount = 3890,
            ratingAvg = 4.9f,
            ratingsCount = 640,
            chaptersCount = 3,
            isPublished = true,
            isFeatured = true,
            isEditorChoice = true
        ),
        StoryEntity(
            id = 2,
            authorId = 4,
            authorName = "زياد المهندس",
            title = "نيو القاهرة 2088: الشفرة المحرمة",
            description = "عندما تصبح الذاكرة البشرية سلعة تُباع في أسواق النيون تحت الأرض، يتورط مهندس شفرات في مؤامرة كبرى تهدد عقول ملايين المواطنين عبر شبكة السيبرانيوم.",
            coverResName = "cover_cyber_city",
            categoryId = "scifi",
            tags = "سايبربانك, خيال علمي, ذكاء اصطناعي, تشويق, مستقبل",
            language = "ar",
            ageRating = "TEEN",
            status = "ONGOING",
            readsCount = 28900,
            likesCount = 2410,
            ratingAvg = 4.8f,
            ratingsCount = 412,
            chaptersCount = 3,
            isPublished = true,
            isFeatured = true,
            isEditorChoice = true
        ),
        StoryEntity(
            id = 3,
            authorId = 3,
            authorName = "سارة نور",
            title = "قهوة في شارع الذكريات",
            description = "رواية رومانسية دافئة تدور أحداثها بين زوايا مقهى قديم في حي الزمالك، حيث يلتقي غريبان وتتشابك رسائل غير مقروءة تركت داخل كتب معارة لسنوات طوال.",
            coverResName = "cover_coffee_memories",
            categoryId = "romance",
            tags = "رومانسية, دراما, ذكريات, حب, هدوء, مقهى",
            language = "ar",
            ageRating = "ALL",
            status = "COMPLETED",
            readsCount = 67300,
            likesCount = 6120,
            ratingAvg = 4.95f,
            ratingsCount = 980,
            chaptersCount = 3,
            isPublished = true,
            isFeatured = true,
            isEditorChoice = false
        ),
        StoryEntity(
            id = 4,
            authorId = 5,
            authorName = "ليلى ناصر",
            title = "غرفة التحقيق رقم ٧",
            description = "جريمة غامضة داخل قصر عتيق مقفل بإحكام خلال عاصفة شتوية. المحقق سامي يواجه ٦ مشتبه بهم، كل منهم يحمل دافعاً وسراً مميتاً. من القاتل الحقيقي؟",
            coverResName = "cover_ancient_mystery",
            categoryId = "mystery",
            tags = "غموض, جريمة, تحقيق, بوليسي, ذكاء, لغز",
            language = "ar",
            ageRating = "TEEN",
            status = "ONGOING",
            readsCount = 38400,
            likesCount = 3150,
            ratingAvg = 4.7f,
            ratingsCount = 520,
            chaptersCount = 3,
            isPublished = true,
            isFeatured = false,
            isEditorChoice = true
        ),
        StoryEntity(
            id = 5,
            authorId = 1,
            authorName = "مينا العبد",
            title = "واحة الأطياف السبعة",
            description = "في عالم انقسم إلى أبعاد لونية سبعة، ينطلق فارس منفي لجمع أطياف النور المفقودة قبل أن يبتلع ظلام العدم مدن الصحراء الساحرة.",
            coverResName = "cover_desert_stars",
            categoryId = "fantasy",
            tags = "فانتازيا, مغامرة, أبعاد, نور, ملحمة",
            language = "ar",
            ageRating = "ALL",
            status = "ONGOING",
            readsCount = 18400,
            likesCount = 1720,
            ratingAvg = 4.85f,
            ratingsCount = 290,
            chaptersCount = 2,
            isPublished = true,
            isFeatured = false,
            isEditorChoice = false
        ),
        StoryEntity(
            id = 6,
            authorId = 3,
            authorName = "سارة نور",
            title = "The Silent Echoes of Rain",
            description = "An emotional contemporary novel exploring second chances, lost letters, and an unforgettable autumn journey across coastal towns.",
            coverResName = "cover_coffee_memories",
            categoryId = "romance",
            tags = "romance, contemporary, emotional, autumn, love",
            language = "en",
            ageRating = "ALL",
            status = "COMPLETED",
            readsCount = 26900,
            likesCount = 2300,
            ratingAvg = 4.75f,
            ratingsCount = 350,
            chaptersCount = 2,
            isPublished = true,
            isFeatured = false,
            isEditorChoice = false
        )
    )

    val chapters = listOf(
        // Story 1: أسرار رمال النجوم
        ChapterEntity(
            id = 1,
            storyId = 1,
            chapterNumber = 1,
            title = "الفصل الأول: البوصلة النحاسية والخريطة الغامضة",
            content = """
                كانت الرياح تعوي بين كثبان الرمال الممتدة كأمواج بحر ذهبي لا نهاية له. وقف «فارس» على قمة التل الرملي، متلفحاً بكوفيته الصوفية المنسوجة، بينما كانت أنامله تتحسس البوصلة النحاسية القديمة التي ورثها عن جده.
                
                > "يا بني، حين تتمايل عقارب هذه البوصلة نحو الشمال الساكن تحت برج الثريا، فاعلم أن الرمال على وشك أن تبوح بسرها المدفون."
                
                تذكر فارس تلك الكلمات بوضوح مذهل. وفجأة، اهتزت الإبرة المعدنية بحدة غير مألوفة، وأصدرت رنيناً خافتاً بدا وكأنه نغمة موسيقية قادمة من زمن سحيق.
                
                توهجت علامات كوكبية دقيقة محفورة على قرص البوصلة بلون أزرق سماوي ناصع. أزاح فارس الرمال بيده عند موضع قدميه، ليجد حجراً مصقولاً أسود كالليل، نُقشت عليه رموز هيروغليفية فلكية تضيء كلما اقتربت منها البوصلة!
                
                «هل يمكن أن تكون هذه هي البوابة التي بحث عنها الرحالة لمئات السنين؟» همس فارس لنفسه والدهشة تشل أطرافه.
            """.trimIndent(),
            wordCount = 142,
            readsCount = 18200,
            likesCount = 1450
        ),
        ChapterEntity(
            id = 2,
            storyId = 1,
            chapterNumber = 2,
            title = "الفصل الثاني: نداء حراس الفلك",
            content = """
                انشقت الأرض ببطء وهدوء يثير الرهبة، متراجعة لتكشف عن درج لولبي من الحجر السماوي يهبط في باطن الصحراء. لم يكن المكان مظلماً كما توقع، بل كانت الجدران مرصعة ببلورات كوارتز مشعة بضياء فضي بارد.
                
                خطا فارس خطوته الأولى بحذر، وسيفه العتيق مستعد بجانبه. كل خطوة كانت تثير أصداء همسات قديمة، أصوات لغات لم تعد منطوقة بين البشر منذ آلاف الأعوام.
                
                في نهاية الدرج، فتحت قاعة مهيبة تعلوها قبة ضخمة تحاكي السماء الحقيقية بنجومها ومجراتها المتحركة في دوران حي متناسق. وفي منتصف القاعة، كان يقف كائن من نور ساطع، يحمل عصا منقوشة بأطوار القمر.
                
                «مرحباً بك يا سليل الفلكيين.. لقد انتظرنا وصول حامل البوصلة طويلاً!»
            """.trimIndent(),
            wordCount = 125,
            readsCount = 14800,
            likesCount = 1220
        ),
        ChapterEntity(
            id = 3,
            storyId = 1,
            chapterNumber = 3,
            title = "الفصل الثالث: العهد المكتوم",
            content = """
                تراجعت هيبة الموقف للحظة حين تقدم الحارس النوراني نحو فارس، كاشفاً عن وجه يفيض بالطمأنينة والحكمة العتيقة.
                
                "المخطوطة التي تبحث عنها ليست مجرد حبر وورق، بل هي نبض النجوم التي توازن طاقة هذا الكوكب. إذا سقطت في يد قراصنة الرمال السوداء، فإن سماء الشرق ستفقد أنوارها إلى الأبد."
                
                مد فارس يده، فاستقرت المخطوطة في قبضته كشعلة دافئة لا تحرق. شعر بتدفق معرفي هائل يسري في عروقه، مدركاً أن رحلته الحقيقية للدفاع عن موطنه قد بدأت للتو.
            """.trimIndent(),
            wordCount = 98,
            readsCount = 12200,
            likesCount = 1100
        ),

        // Story 2: نيو القاهرة 2088
        ChapterEntity(
            id = 4,
            storyId = 2,
            chapterNumber = 1,
            title = "الفصل الأول: أمطار النيون في الحي السفلي",
            content = """
                كانت أضواء النيون البنفسجية والزرقاء تنعكس على مياه الأمطار المتجمعة في أزقة نيو القاهرة السفلية. ركض «عمر» وهو يشد معطفه الجلدي الإلكتروني المقاوم للحرارة، وعيناه تراقبان شاشة الواجهة العصبية الاصطناعية المزروعة في عدسته اليمنى.
                
                **[تحذير النظام: تم رصد تعقب أمني من الدرجة الثالثة]**
                
                «اللعنة، لقد اكتشفوا تسريب الذاكرة بهذه السرعة!» تمتم عمر وهو ينعطف في نفق جانبي تفوح منه رائحة الأوزون والأسلاك المحترقة.
                
                في جيبه الداخلي، كانت الشريحة الكمومية تنبض بحرارة متزايدة. شريحة تحتوي على تسجيلات سرية لمشروع "عقول بلا حدود".. المؤامرة التي خططت لها كبرى شركات التكنولوجيا للسيطرة على الإرادة الحرة للبشر.
            """.trimIndent(),
            wordCount = 110,
            readsCount = 11200,
            likesCount = 980
        ),
        ChapterEntity(
            id = 5,
            storyId = 2,
            chapterNumber = 2,
            title = "الفصل الثاني: ملاذ القراصنة المشفر",
            content = """
                وصل عمر أخيراً إلى الباب الحديدي المموه خلف متجر روبوتات قديم. نقر على اللوحة الحيوية بنمط مشفر، فانفتح الباب بصرير ميكانيكي ليدخله إلى مختبر سرّي مليء بالشاشات ثلاثية الأبعاد وقطع الهاردوير المتناثرة.
                
                استقبلته «مايا»، أفضل مخترقة نظم في المدينة، وهي تعدل نظارات الواقع الممتد الخاصة بها.
                
                «هل جلبتها معك يا عمر؟» سألت بلهفة.
                
                «نعم، لكنهم خلفي مباشرة.. لدينا أقل من عشر دقائق لفك التشفير قبل أن تقتحم قوات الدرع السيبراني هذا المكان!»
            """.trimIndent(),
            wordCount = 90,
            readsCount = 9400,
            likesCount = 820
        ),
        ChapterEntity(
            id = 6,
            storyId = 2,
            chapterNumber = 3,
            title = "الفصل الثالث: الحقيقة خلف الستار",
            content = """
                تم توصيل الشريحة بجهاز التحليل الجزيئي. أضاءت الغرفة بأسراب من البيانات الرقمية الطافية في الهواء كشرارات من الضوء الساحر.
                
                ظهر تسجيل مجسم لرئيس المؤسسة العملاقة وهو يوقع أمر التفعيل الشامل. لم يكن الهدف السيطرة التجارية فحسب، بل محو ذكريات الثقافة والتاريخ وإعادة برمجة الهوية الإنسانية بالكامل.
                
                نظرت مايا إلى عمر بعيون يملؤها الإصرار: «سنبث هذا التسجيل إلى كل شاشات المدينة الليلة.. حان وقت استيقاظ العالم.»
            """.trimIndent(),
            wordCount = 85,
            readsCount = 8300,
            likesCount = 610
        ),

        // Story 3: قهوة في شارع الذكريات
        ChapterEntity(
            id = 7,
            storyId = 3,
            chapterNumber = 1,
            title = "الفصل الأول: طاولة بجوار النافذة العتيقة",
            content = """
                كان لرائحة القهوة الطازجة الممزوجة بنكهة الهيل أثر ساحر في مقهى «ألحان الزمان». اعتادت «ندى» أن تجلس كل خميس عند الطاولة الخشبية ذاتها المطلة على أشجار الجاكراندا البنفسجية في حي الزمالك العريق.
                
                فتحت كتابها المفضل، رواية قديمة استعارتها من رف المقهى المجاني. وحين قلبت الصفحة الأربعين، سقطت قصاصة ورق صفراء مكتوبة بخط يد أنيق وواضح:
                
                > «إلى من يقرأ هذه الكلمات في زمن لم أعد فيه هنا.. أتمنى أن تجد في هذا المقهى السلوى التي بحثت عنها طويلاً، وأن تؤمن بأن المصادفات الجميلة هي مواعيد قدرية لم نتفق عليها.»
                
                توقفت أنفاس ندى وهي تقرأ التاريخ في أسفل القصاصة.. لقد كتبت قبل عشرين عاماً بالضبط في نفس هذا اليوم!
            """.trimIndent(),
            wordCount = 120,
            readsCount = 28000,
            likesCount = 2800
        ),
        ChapterEntity(
            id = 8,
            storyId = 3,
            chapterNumber = 2,
            title = "الفصل الثاني: صاحب الرسائل المنسية",
            content = """
                لم تستطع ندى مقاومة الفضول. سألت العم «محمود»، صاحب المقهى العجوز ذي الابتسامة الأبوية، عن صاحب ذلك الخط المميز.
                
                ابتسم العم محمود بحنين ومسح زجاج نظارته: «آه.. إنه خط كريم، كان كاتباً شاباً اعتاد الجلوس هنا لساعات يكتب في دفاتره الجلدية قبل أن يسافر بعيداً.. لكنه ترك صندوقاً صغيراً من الذكريات قال إنه أمانة لمن يفهم لغة كتبه.»
                
                ناولها العم محمود الصندوق الخشبي المزخرف بالأرابيسك. وعندما فتحته، وجدت ندى دفتراً لم يكتمل، وأول سطر فيه كان يخاطب قارئاً يملك عيوناً فضولية تماماً كعيونها.
            """.trimIndent(),
            wordCount = 95,
            readsCount = 21000,
            likesCount = 2100
        ),
        ChapterEntity(
            id = 9,
            storyId = 3,
            chapterNumber = 3,
            title = "الفصل الثالث: موعد مع الغد",
            content = """
                مرت الأسابيع وندى تكمل السطور المفقودة من رواية كريم، حتى جاء ذلك المساء الماطر. دخل شاب طويل يحمل مظلة مبللة ومعطفاً كلاسيكياً، وتوجه بخطوات واثقة نحو نفس الطاولة.
                
                «عفواً.. هل يمكنني الجلوس هنا؟ هذا المكان يذكرني بوالدي كثيراً.»
                
                رفعت ندى رأسها، ورأت في عينيه نفس النظرة الهادئة الموصوفة في صفحات الدفتر، لتبدأ قصة جديدة كُتبت حروفها بين عبق القهوة وقطرات المطر.
            """.trimIndent(),
            wordCount = 80,
            readsCount = 18300,
            likesCount = 1220
        ),

        // Story 4: غرفة التحقيق رقم ٧
        ChapterEntity(
            id = 10,
            storyId = 4,
            chapterNumber = 1,
            title = "الفصل الأول: انقطاع التيار والصرخة",
            content = """
                كانت الساعة تشير إلى منتصف الليل تماماً حين انقطع التيار الكهربائي عن قصر «البارون فؤاد». ساد ظلام دامس لثلاث دقائق فقط، قطعتها صرخة مدوية هزت أركان القاعة الكبرى.
                
                حين عادت الأضواء ببطء من المولد الاحتياطي، كان البارون ملقى على كرسيه الجلدي الفاخر فاقداً للحياة، وبجانبه كأس عصير محطم، وخاتمه الياقوتي الشهير مفقود من أصبعه!
                
                أغلق المحقق «سامي» جميع مخارج القصر على الفور ونظر إلى الوجوه الشاحبة أمامه:
                «لا أحد يغادر هذه الغرفة.. القاتل بيننا الآن، ولديه حجة غياب مزيفة سأكشفها قبل شروق الشمس!»
            """.trimIndent(),
            wordCount = 100,
            readsCount = 15400,
            likesCount = 1300
        ),
        ChapterEntity(
            id = 11,
            storyId = 4,
            chapterNumber = 2,
            title = "الفصل الثاني: شهادات متناقضة",
            content = """
                بدأ المحقق سامي استجواب المشتبه بهم واحداً تلو الآخر في غرفة المكتبة الهادئة.
                
                الزوجة ادعت أنها كانت تتفقد شمعة الممر، لكن آثار الطين الرطب على حذائها تشير إلى أنها خرجت للحديقة الخلفية في ذروة العاصفة.
                أما الطبيب الخاص، فكانت يداه ترتجفان بشكل مريب وهو يدعي أنه حاول إنقاذ البارون بالحقنة المهدئة.
                
                «كل إجابة تقدمونها تقربني من الحقيقة أكثر مما تظنون،» قال سامي وهو يدون ملاحظاته بروية في دفتره الأسود.
            """.trimIndent(),
            wordCount = 85,
            readsCount = 12600,
            likesCount = 1050
        ),
        ChapterEntity(
            id = 12,
            storyId = 4,
            chapterNumber = 3,
            title = "الفصل الثالث: الخاتم المسموم",
            content = """
                طلب سامي فحص الكأس المحطم تحت المجهر الجنائي المحمول.
                «البارون لم يمت بالسم في العصير كما اعتقدتم جميعاً.. بل تم دهن سن الخاتم الياقوتي بمادة شلت حركته فور لمسها، ثم قام الجاني بنزع الخاتم لإخفاء الأداة!»
                
                اتجهت عيون الجميع نحو السكرتير الشاب الذي حاول فجأة الاندفاع نحو النافذة، لكن رجال الشرطة كانوا أسرع في تقييده، لتسدل الستار على أعقد قضايا الموسم.
            """.trimIndent(),
            wordCount = 80,
            readsCount = 10400,
            likesCount = 800
        )
    )

    val comments = listOf(
        CommentEntity(
            id = 1,
            storyId = 1,
            chapterId = 1,
            userId = 7,
            userName = "أحمد خالد",
            content = "بداية ملحمية مشوقة جداً! وصف الصحراء وحركة البوصلة أخذني لعالم آخر تماماً.",
            likesCount = 24
        ),
        CommentEntity(
            id = 2,
            storyId = 1,
            chapterId = 1,
            userId = 3,
            userName = "سارة نور",
            content = "أسلوب سردي ممتع ومتقن يا طارق! ننتظر معرفة سر الكائن النوراني.",
            likesCount = 18
        ),
        CommentEntity(
            id = 3,
            storyId = 3,
            chapterId = 1,
            userId = 1,
            userName = "مينا العبد",
            content = "رواية دافئة تلامس الروح.. تفاصيل مقهى الزمالك تجعل القارئ يشتم رائحة القهوة مع كل سطر!",
            likesCount = 42
        ),
        CommentEntity(
            id = 4,
            storyId = 2,
            chapterId = 1,
            userId = 7,
            userName = "أحمد خالد",
            content = "أجواء السايبربانك في نيو القاهرة فكرة عبقرية ومبتكرة جداً في الأدب العربي المعاصر.",
            likesCount = 15
        )
    )

    val ratings = listOf(
        RatingEntity(id = 1, userId = 7, storyId = 1, stars = 5, review = "من أروع الروايات الخيالية التي قرأتها في المنصة! حبكة متماسكة وشخصيات حية."),
        RatingEntity(id = 2, userId = 3, storyId = 1, stars = 5, review = "إبداع لا يوصف وتصوير فني عبقري لعوالم النجوم والصحراء."),
        RatingEntity(id = 3, userId = 1, storyId = 3, stars = 5, review = "تحفة رومانسية هادئة تعيد الإيمان بالجمال والكلمة الصادقة."),
        RatingEntity(id = 4, userId = 7, storyId = 2, stars = 5, review = "رؤية مستقبلية مذهلة وسرعة أحداث تحبس الأنفاس.")
    )

    val notifications = listOf(
        NotificationEntity(
            id = 1,
            userId = 1,
            senderName = "طارق عمران",
            title = "فصل جديد متوفر الآن",
            message = "قام الكاتب طارق عمران بنشر الفصل الثالث من رواية «أسرار رمال النجوم».",
            type = "CHAPTER_UPDATE",
            targetId = 1,
            isRead = false
        ),
        NotificationEntity(
            id = 2,
            userId = 1,
            senderName = "سارة نور",
            title = "متابع جديد",
            message = "بدأت الكاتبة سارة نور بمتابعة حسابك.",
            type = "FOLLOW",
            targetId = 3,
            isRead = false
        ),
        NotificationEntity(
            id = 3,
            userId = 1,
            senderName = "فريق قصة",
            title = "إنجاز جديد: 10,000 قارئ!",
            message = "مبروك! تجاوزت قصصك حاجز الـ 10,000 قراءة عبر المنصة.",
            type = "ACHIEVEMENT",
            targetId = null,
            isRead = true
        )
    )

    val initialLibraries = listOf(
        LibraryItemEntity(id = 1, userId = 1, storyId = 1, readingList = "CURRENT"),
        LibraryItemEntity(id = 2, userId = 1, storyId = 3, readingList = "FAVORITES"),
        LibraryItemEntity(id = 3, userId = 1, storyId = 2, readingList = "LATER")
    )

    val initialProgress = listOf(
        ReadingProgressEntity(id = 1, userId = 1, storyId = 1, lastChapterId = 1, lastChapterNumber = 1, scrollProgress = 0.45f),
        ReadingProgressEntity(id = 2, userId = 1, storyId = 3, lastChapterId = 7, lastChapterNumber = 1, scrollProgress = 0.8f)
    )

    val reports = listOf(
        ReportEntity(
            id = 1,
            reporterId = 7,
            reporterName = "أحمد خالد",
            targetType = "COMMENT",
            targetId = 99,
            targetTitle = "تعليق إعلاني مكرر",
            reason = "محتوى دعائي غير لائق مكرر في عدة فصول",
            status = "PENDING"
        ),
        ReportEntity(
            id = 2,
            reporterId = 5,
            reporterName = "ليلى ناصر",
            targetType = "STORY",
            targetId = 101,
            targetTitle = "قصة غير مصنفة الفئة العمرية",
            reason = "تحتوي على مشاهد عنف حادة دون وسم الفئة العمرية المناسبة",
            status = "PENDING"
        )
    )
}
