#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class DLDDSDKDataDonationSDKPublicAPIEnvironment, DLDDSDKKotlinEnum<E>, DLDDSDKConsentDataContractConsentEvent, DLDDSDKKotlinArray<T>, DLDDSDKKotlinException, DLDDSDKConsentDocument, DLDDSDKUserConsent, DLDDSDKKotlinThrowable, DLDDSDKKotlinRuntimeException, DLDDSDKUtilD4LRuntimeException, DLDDSDKConsentServiceError, DLDDSDKCoreRuntimeError, DLDDSDKKotlinx_coroutines_coreCancellationException, DLDDSDKKotlinUnit, DLDDSDKKotlinx_serialization_coreSerializersModule, DLDDSDKKotlinx_serialization_coreSerialKind, DLDDSDKKotlinNothing, DLDDSDKKotlinIllegalStateException, DLDDSDKKotlinx_coroutines_coreAtomicDesc, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp, DLDDSDKKotlinx_coroutines_coreAtomicOp<__contravariant T>, DLDDSDKKotlinx_coroutines_coreOpDescriptor, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAddLastDesc<T>, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeRemoveFirstDesc<T>;

@protocol DLDDSDKUtil_coroutineD4LSDKFlowContract, DLDDSDKDataDonationSDKPublicAPIDataDonationClient, DLDDSDKDataDonationSDKPublicAPIUserSessionTokenProvider, DLDDSDKDataDonationSDKPublicAPIDataDonationClientFactory, DLDDSDKKotlinComparable, DLDDSDKKotlinx_serialization_coreKSerializer, DLDDSDKConsentDataContractConsentDocument, DLDDSDKConsentDataContractUserConsent, DLDDSDKKotlinx_coroutines_coreJob, DLDDSDKKotlinx_coroutines_coreCoroutineScope, DLDDSDKKotlinx_coroutines_coreFlow, DLDDSDKKotlinIterator, DLDDSDKKotlinx_serialization_coreEncoder, DLDDSDKKotlinx_serialization_coreSerialDescriptor, DLDDSDKKotlinx_serialization_coreSerializationStrategy, DLDDSDKKotlinx_serialization_coreDecoder, DLDDSDKKotlinx_serialization_coreDeserializationStrategy, DLDDSDKKotlinx_coroutines_coreChildHandle, DLDDSDKKotlinx_coroutines_coreChildJob, DLDDSDKKotlinx_coroutines_coreDisposableHandle, DLDDSDKKotlinSequence, DLDDSDKKotlinx_coroutines_coreSelectClause0, DLDDSDKKotlinCoroutineContextKey, DLDDSDKKotlinCoroutineContextElement, DLDDSDKKotlinCoroutineContext, DLDDSDKKotlinx_coroutines_coreFlowCollector, DLDDSDKKotlinx_serialization_coreCompositeEncoder, DLDDSDKKotlinAnnotation, DLDDSDKKotlinx_serialization_coreCompositeDecoder, DLDDSDKKotlinx_coroutines_coreParentJob, DLDDSDKKotlinx_coroutines_coreSelectInstance, DLDDSDKKotlinSuspendFunction0, DLDDSDKKotlinx_serialization_coreSerializersModuleCollector, DLDDSDKKotlinKClass, DLDDSDKKotlinContinuation, DLDDSDKKotlinFunction, DLDDSDKKotlinKDeclarationContainer, DLDDSDKKotlinKAnnotatedElement, DLDDSDKKotlinKClassifier;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

__attribute__((swift_name("KotlinBase")))
@interface DLDDSDKBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface DLDDSDKBase (DLDDSDKBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface DLDDSDKMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface DLDDSDKMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorDLDDSDKKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface DLDDSDKNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface DLDDSDKByte : DLDDSDKNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface DLDDSDKUByte : DLDDSDKNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface DLDDSDKShort : DLDDSDKNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface DLDDSDKUShort : DLDDSDKNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface DLDDSDKInt : DLDDSDKNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface DLDDSDKUInt : DLDDSDKNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface DLDDSDKLong : DLDDSDKNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface DLDDSDKULong : DLDDSDKNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface DLDDSDKFloat : DLDDSDKNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface DLDDSDKDouble : DLDDSDKNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface DLDDSDKBoolean : DLDDSDKNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((swift_name("DataDonationSDKPublicAPIDataDonationClient")))
@protocol DLDDSDKDataDonationSDKPublicAPIDataDonationClient
@required
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)createUserConsentConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(int32_t)consentDocumentVersion __attribute__((swift_name("createUserConsent(consentDocumentKey:consentDocumentVersion:)")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)fetchAllUserConsents __attribute__((swift_name("fetchAllUserConsents()")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)fetchConsentDocumentsConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(DLDDSDKInt * _Nullable)consentDocumentVersion language:(NSString * _Nullable)language __attribute__((swift_name("fetchConsentDocuments(consentDocumentKey:consentDocumentVersion:language:)")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)fetchUserConsentsConsentDocumentKey:(NSString *)consentDocumentKey __attribute__((swift_name("fetchUserConsents(consentDocumentKey:)")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)revokeUserConsentConsentDocumentKey:(NSString *)consentDocumentKey __attribute__((swift_name("revokeUserConsent(consentDocumentKey:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Client")))
@interface DLDDSDKClient : DLDDSDKBase <DLDDSDKDataDonationSDKPublicAPIDataDonationClient>
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)createUserConsentConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(int32_t)consentDocumentVersion __attribute__((swift_name("createUserConsent(consentDocumentKey:consentDocumentVersion:)")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)fetchAllUserConsents __attribute__((swift_name("fetchAllUserConsents()")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)fetchConsentDocumentsConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(DLDDSDKInt * _Nullable)consentDocumentVersion language:(NSString * _Nullable)language __attribute__((swift_name("fetchConsentDocuments(consentDocumentKey:consentDocumentVersion:language:)")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)fetchUserConsentsConsentDocumentKey:(NSString *)consentDocumentKey __attribute__((swift_name("fetchUserConsents(consentDocumentKey:)")));
- (id<DLDDSDKUtil_coroutineD4LSDKFlowContract>)revokeUserConsentConsentDocumentKey:(NSString *)consentDocumentKey __attribute__((swift_name("revokeUserConsent(consentDocumentKey:)")));
@end;

__attribute__((swift_name("DataDonationSDKPublicAPIDataDonationClientFactory")))
@protocol DLDDSDKDataDonationSDKPublicAPIDataDonationClientFactory
@required
- (id<DLDDSDKDataDonationSDKPublicAPIDataDonationClient>)getInstanceEnvironment:(DLDDSDKDataDonationSDKPublicAPIEnvironment *)environment userSession:(id<DLDDSDKDataDonationSDKPublicAPIUserSessionTokenProvider>)userSession __attribute__((swift_name("getInstance(environment:userSession:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Client.Factory")))
@interface DLDDSDKClientFactory : DLDDSDKBase <DLDDSDKDataDonationSDKPublicAPIDataDonationClientFactory>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)factory __attribute__((swift_name("init()")));
- (id<DLDDSDKDataDonationSDKPublicAPIDataDonationClient>)getInstanceEnvironment:(DLDDSDKDataDonationSDKPublicAPIEnvironment *)environment userSession:(id<DLDDSDKDataDonationSDKPublicAPIUserSessionTokenProvider>)userSession __attribute__((swift_name("getInstance(environment:userSession:)")));
@end;

__attribute__((swift_name("ConsentDataContract")))
@protocol DLDDSDKConsentDataContract
@required
@end;

__attribute__((swift_name("ConsentDataContractConsentDocument")))
@protocol DLDDSDKConsentDataContractConsentDocument
@required
@property (readonly) BOOL allowsAdminConsent __attribute__((swift_name("allowsAdminConsent")));
@property (readonly) NSString * _Nullable consentEmailTemplateKey __attribute__((swift_name("consentEmailTemplateKey")));
@property (readonly) NSString * _Nullable description_ __attribute__((swift_name("description_")));
@property (readonly) BOOL isIrrevocable __attribute__((swift_name("isIrrevocable")));
@property (readonly) NSString *key __attribute__((swift_name("key")));
@property (readonly) NSString *language __attribute__((swift_name("language")));
@property (readonly) NSString *processor __attribute__((swift_name("processor")));
@property (readonly) NSString *recipient __attribute__((swift_name("recipient")));
@property (readonly) NSString * _Nullable revokeEmailTemplateKey __attribute__((swift_name("revokeEmailTemplateKey")));
@property (readonly) NSString *text __attribute__((swift_name("text")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol DLDDSDKKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface DLDDSDKKotlinEnum<E> : DLDDSDKBase <DLDDSDKKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentDataContractConsentEvent")))
@interface DLDDSDKConsentDataContractConsentEvent : DLDDSDKKotlinEnum<DLDDSDKConsentDataContractConsentEvent *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKConsentDataContractConsentEvent *consent __attribute__((swift_name("consent")));
@property (class, readonly) DLDDSDKConsentDataContractConsentEvent *revoke __attribute__((swift_name("revoke")));
+ (DLDDSDKKotlinArray<DLDDSDKConsentDataContractConsentEvent *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentDataContractConsentEvent.Companion")))
@interface DLDDSDKConsentDataContractConsentEventCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("ConsentDataContractUserConsent")))
@protocol DLDDSDKConsentDataContractUserConsent
@required
@property (readonly) NSString *accountId __attribute__((swift_name("accountId")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString *consentDocumentVersion __attribute__((swift_name("consentDocumentVersion")));
@property (readonly) NSString *createdAt __attribute__((swift_name("createdAt")));
@property (readonly) DLDDSDKConsentDataContractConsentEvent *event __attribute__((swift_name("event")));
@end;

__attribute__((swift_name("DataDonationSDKPublicAPI")))
@protocol DLDDSDKDataDonationSDKPublicAPI
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DataDonationSDKPublicAPIEnvironment")))
@interface DLDDSDKDataDonationSDKPublicAPIEnvironment : DLDDSDKKotlinEnum<DLDDSDKDataDonationSDKPublicAPIEnvironment *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKDataDonationSDKPublicAPIEnvironment *dev __attribute__((swift_name("dev")));
@property (class, readonly) DLDDSDKDataDonationSDKPublicAPIEnvironment *sandbox __attribute__((swift_name("sandbox")));
@property (class, readonly) DLDDSDKDataDonationSDKPublicAPIEnvironment *staging __attribute__((swift_name("staging")));
@property (class, readonly) DLDDSDKDataDonationSDKPublicAPIEnvironment *production __attribute__((swift_name("production")));
+ (DLDDSDKKotlinArray<DLDDSDKDataDonationSDKPublicAPIEnvironment *> *)values __attribute__((swift_name("values()")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((swift_name("DataDonationSDKPublicAPIUserSessionTokenProvider")))
@protocol DLDDSDKDataDonationSDKPublicAPIUserSessionTokenProvider
@required
- (void)getUserSessionTokenOnSuccess:(void (^)(NSString *))onSuccess onError:(void (^)(DLDDSDKKotlinException *))onError __attribute__((swift_name("getUserSessionToken(onSuccess:onError:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentDocument")))
@interface DLDDSDKConsentDocument : DLDDSDKBase <DLDDSDKConsentDataContractConsentDocument>
- (instancetype)initWithKey:(NSString *)key version:(int32_t)version processor:(NSString *)processor description:(NSString * _Nullable)description recipient:(NSString *)recipient language:(NSString *)language text:(NSString *)text allowsAdminConsent:(BOOL)allowsAdminConsent isIrrevocable:(BOOL)isIrrevocable consentEmailTemplateKey:(NSString * _Nullable)consentEmailTemplateKey revokeEmailTemplateKey:(NSString * _Nullable)revokeEmailTemplateKey __attribute__((swift_name("init(key:version:processor:description:recipient:language:text:allowsAdminConsent:isIrrevocable:consentEmailTemplateKey:revokeEmailTemplateKey:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component10 __attribute__((swift_name("component10()")));
- (NSString * _Nullable)component11 __attribute__((swift_name("component11()")));
- (int32_t)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (NSString *)component6 __attribute__((swift_name("component6()")));
- (NSString *)component7 __attribute__((swift_name("component7()")));
- (BOOL)component8 __attribute__((swift_name("component8()")));
- (BOOL)component9 __attribute__((swift_name("component9()")));
- (DLDDSDKConsentDocument *)doCopyKey:(NSString *)key version:(int32_t)version processor:(NSString *)processor description:(NSString * _Nullable)description recipient:(NSString *)recipient language:(NSString *)language text:(NSString *)text allowsAdminConsent:(BOOL)allowsAdminConsent isIrrevocable:(BOOL)isIrrevocable consentEmailTemplateKey:(NSString * _Nullable)consentEmailTemplateKey revokeEmailTemplateKey:(NSString * _Nullable)revokeEmailTemplateKey __attribute__((swift_name("doCopy(key:version:processor:description:recipient:language:text:allowsAdminConsent:isIrrevocable:consentEmailTemplateKey:revokeEmailTemplateKey:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL allowsAdminConsent __attribute__((swift_name("allowsAdminConsent")));
@property (readonly) NSString * _Nullable consentEmailTemplateKey __attribute__((swift_name("consentEmailTemplateKey")));
@property (readonly) NSString * _Nullable description_ __attribute__((swift_name("description_")));
@property (readonly) BOOL isIrrevocable __attribute__((swift_name("isIrrevocable")));
@property (readonly) NSString *key __attribute__((swift_name("key")));
@property (readonly) NSString *language __attribute__((swift_name("language")));
@property (readonly) NSString *processor __attribute__((swift_name("processor")));
@property (readonly) NSString *recipient __attribute__((swift_name("recipient")));
@property (readonly) NSString * _Nullable revokeEmailTemplateKey __attribute__((swift_name("revokeEmailTemplateKey")));
@property (readonly) NSString *text __attribute__((swift_name("text")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentDocument.Companion")))
@interface DLDDSDKConsentDocumentCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserConsent")))
@interface DLDDSDKUserConsent : DLDDSDKBase <DLDDSDKConsentDataContractUserConsent>
- (instancetype)initWithConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(NSString *)consentDocumentVersion accountId:(NSString *)accountId event:(DLDDSDKConsentDataContractConsentEvent *)event createdAt:(NSString *)createdAt __attribute__((swift_name("init(consentDocumentKey:consentDocumentVersion:accountId:event:createdAt:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKConsentDataContractConsentEvent *)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (DLDDSDKUserConsent *)doCopyConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(NSString *)consentDocumentVersion accountId:(NSString *)accountId event:(DLDDSDKConsentDataContractConsentEvent *)event createdAt:(NSString *)createdAt __attribute__((swift_name("doCopy(consentDocumentKey:consentDocumentVersion:accountId:event:createdAt:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *accountId __attribute__((swift_name("accountId")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString *consentDocumentVersion __attribute__((swift_name("consentDocumentVersion")));
@property (readonly) NSString *createdAt __attribute__((swift_name("createdAt")));
@property (readonly) DLDDSDKConsentDataContractConsentEvent *event __attribute__((swift_name("event")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserConsent.Companion")))
@interface DLDDSDKUserConsentCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("KotlinThrowable")))
@interface DLDDSDKKotlinThrowable : DLDDSDKBase
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) DLDDSDKKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
@end;

__attribute__((swift_name("KotlinException")))
@interface DLDDSDKKotlinException : DLDDSDKKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinRuntimeException")))
@interface DLDDSDKKotlinRuntimeException : DLDDSDKKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("UtilD4LRuntimeException")))
@interface DLDDSDKUtilD4LRuntimeException : DLDDSDKKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("ConsentServiceError")))
@interface DLDDSDKConsentServiceError : DLDDSDKUtilD4LRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (readonly) int32_t httpStatus __attribute__((swift_name("httpStatus")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.BadRequest")))
@interface DLDDSDKConsentServiceErrorBadRequest : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.DocumentConflict")))
@interface DLDDSDKConsentServiceErrorDocumentConflict : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.Forbidden")))
@interface DLDDSDKConsentServiceErrorForbidden : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.InternalServer")))
@interface DLDDSDKConsentServiceErrorInternalServer : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.NotFound")))
@interface DLDDSDKConsentServiceErrorNotFound : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.TooManyRequests")))
@interface DLDDSDKConsentServiceErrorTooManyRequests : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.Unauthorized")))
@interface DLDDSDKConsentServiceErrorUnauthorized : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.UnexpectedFailure")))
@interface DLDDSDKConsentServiceErrorUnexpectedFailure : DLDDSDKConsentServiceError
- (instancetype)initWithHttpStatus:(int32_t)httpStatus __attribute__((swift_name("init(httpStatus:)"))) __attribute__((objc_designated_initializer));
@property (readonly) int32_t httpStatus __attribute__((swift_name("httpStatus")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentServiceError.UnprocessableEntity")))
@interface DLDDSDKConsentServiceErrorUnprocessableEntity : DLDDSDKConsentServiceError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((swift_name("CoreRuntimeError")))
@interface DLDDSDKCoreRuntimeError : DLDDSDKUtilD4LRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoreRuntimeError.InternalFailure")))
@interface DLDDSDKCoreRuntimeErrorInternalFailure : DLDDSDKCoreRuntimeError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoreRuntimeError.MissingCredentials")))
@interface DLDDSDKCoreRuntimeErrorMissingCredentials : DLDDSDKCoreRuntimeError
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoreRuntimeError.MissingSession")))
@interface DLDDSDKCoreRuntimeErrorMissingSession : DLDDSDKCoreRuntimeError
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoreRuntimeError.RequestValidationFailure")))
@interface DLDDSDKCoreRuntimeErrorRequestValidationFailure : DLDDSDKCoreRuntimeError
- (instancetype)initWithMessage:(NSString *)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoreRuntimeError.ResponseTransformFailure")))
@interface DLDDSDKCoreRuntimeErrorResponseTransformFailure : DLDDSDKCoreRuntimeError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((swift_name("Util_coroutineD4LSDKFlowContract")))
@protocol DLDDSDKUtil_coroutineD4LSDKFlowContract
@required
- (id<DLDDSDKKotlinx_coroutines_coreJob>)subscribeScope:(id<DLDDSDKKotlinx_coroutines_coreCoroutineScope>)scope onEach:(void (^)(id _Nullable))onEach onError:(void (^)(DLDDSDKKotlinThrowable *))onError onComplete:(void (^ _Nullable)(void))onComplete __attribute__((swift_name("subscribe(scope:onEach:onError:onComplete:)")));
@property (readonly) id<DLDDSDKKotlinx_coroutines_coreFlow> ktFlow __attribute__((swift_name("ktFlow")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface DLDDSDKKotlinArray<T> : DLDDSDKBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(DLDDSDKInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<DLDDSDKKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol DLDDSDKKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<DLDDSDKKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<DLDDSDKKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol DLDDSDKKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<DLDDSDKKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<DLDDSDKKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol DLDDSDKKotlinx_serialization_coreKSerializer <DLDDSDKKotlinx_serialization_coreSerializationStrategy, DLDDSDKKotlinx_serialization_coreDeserializationStrategy>
@required
@end;

__attribute__((swift_name("KotlinCoroutineContext")))
@protocol DLDDSDKKotlinCoroutineContext
@required
- (id _Nullable)foldInitial:(id _Nullable)initial operation:(id _Nullable (^)(id _Nullable, id<DLDDSDKKotlinCoroutineContextElement>))operation __attribute__((swift_name("fold(initial:operation:)")));
- (id<DLDDSDKKotlinCoroutineContextElement> _Nullable)getKey:(id<DLDDSDKKotlinCoroutineContextKey>)key __attribute__((swift_name("get(key:)")));
- (id<DLDDSDKKotlinCoroutineContext>)minusKeyKey:(id<DLDDSDKKotlinCoroutineContextKey>)key __attribute__((swift_name("minusKey(key:)")));
- (id<DLDDSDKKotlinCoroutineContext>)plusContext:(id<DLDDSDKKotlinCoroutineContext>)context __attribute__((swift_name("plus(context:)")));
@end;

__attribute__((swift_name("KotlinCoroutineContextElement")))
@protocol DLDDSDKKotlinCoroutineContextElement <DLDDSDKKotlinCoroutineContext>
@required
@property (readonly) id<DLDDSDKKotlinCoroutineContextKey> key __attribute__((swift_name("key")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreJob")))
@protocol DLDDSDKKotlinx_coroutines_coreJob <DLDDSDKKotlinCoroutineContextElement>
@required
- (id<DLDDSDKKotlinx_coroutines_coreChildHandle>)attachChildChild:(id<DLDDSDKKotlinx_coroutines_coreChildJob>)child __attribute__((swift_name("attachChild(child:)")));
- (void)cancelCause:(DLDDSDKKotlinx_coroutines_coreCancellationException * _Nullable)cause __attribute__((swift_name("cancel(cause:)")));
- (DLDDSDKKotlinx_coroutines_coreCancellationException *)getCancellationException __attribute__((swift_name("getCancellationException()")));
- (id<DLDDSDKKotlinx_coroutines_coreDisposableHandle>)invokeOnCompletionOnCancelling:(BOOL)onCancelling invokeImmediately:(BOOL)invokeImmediately handler:(void (^)(DLDDSDKKotlinThrowable * _Nullable))handler __attribute__((swift_name("invokeOnCompletion(onCancelling:invokeImmediately:handler:)")));
- (id<DLDDSDKKotlinx_coroutines_coreDisposableHandle>)invokeOnCompletionHandler:(void (^)(DLDDSDKKotlinThrowable * _Nullable))handler __attribute__((swift_name("invokeOnCompletion(handler:)")));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)joinWithCompletionHandler:(void (^)(DLDDSDKKotlinUnit * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("join(completionHandler:)")));
- (id<DLDDSDKKotlinx_coroutines_coreJob>)plusOther:(id<DLDDSDKKotlinx_coroutines_coreJob>)other __attribute__((swift_name("plus(other:)"))) __attribute__((unavailable("Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")));
- (BOOL)start __attribute__((swift_name("start()")));
@property (readonly) id<DLDDSDKKotlinSequence> children __attribute__((swift_name("children")));
@property (readonly) BOOL isActive __attribute__((swift_name("isActive")));
@property (readonly) BOOL isCancelled __attribute__((swift_name("isCancelled")));
@property (readonly) BOOL isCompleted __attribute__((swift_name("isCompleted")));
@property (readonly) id<DLDDSDKKotlinx_coroutines_coreSelectClause0> onJoin __attribute__((swift_name("onJoin")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreCoroutineScope")))
@protocol DLDDSDKKotlinx_coroutines_coreCoroutineScope
@required
@property (readonly) id<DLDDSDKKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreFlow")))
@protocol DLDDSDKKotlinx_coroutines_coreFlow
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<DLDDSDKKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(DLDDSDKKotlinUnit * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol DLDDSDKKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol DLDDSDKKotlinx_serialization_coreEncoder
@required
- (id<DLDDSDKKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<DLDDSDKKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<DLDDSDKKotlinx_serialization_coreEncoder>)encodeInlineInlineDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)inlineDescriptor __attribute__((swift_name("encodeInline(inlineDescriptor:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));
- (void)encodeNull __attribute__((swift_name("encodeNull()")));
- (void)encodeNullableSerializableValueSerializer:(id<DLDDSDKKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<DLDDSDKKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) DLDDSDKKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol DLDDSDKKotlinx_serialization_coreSerialDescriptor
@required
- (NSArray<id<DLDDSDKKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));
- (id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));
@property (readonly) NSArray<id<DLDDSDKKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isInline __attribute__((swift_name("isInline")));
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));
@property (readonly) DLDDSDKKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol DLDDSDKKotlinx_serialization_coreDecoder
@required
- (id<DLDDSDKKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<DLDDSDKKotlinx_serialization_coreDecoder>)decodeInlineInlineDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)inlineDescriptor __attribute__((swift_name("decodeInline(inlineDescriptor:)")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));
- (DLDDSDKKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<DLDDSDKKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<DLDDSDKKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) DLDDSDKKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("KotlinIllegalStateException")))
@interface DLDDSDKKotlinIllegalStateException : DLDDSDKKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinCancellationException")))
@interface DLDDSDKKotlinCancellationException : DLDDSDKKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreDisposableHandle")))
@protocol DLDDSDKKotlinx_coroutines_coreDisposableHandle
@required
- (void)dispose __attribute__((swift_name("dispose()")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreChildHandle")))
@protocol DLDDSDKKotlinx_coroutines_coreChildHandle <DLDDSDKKotlinx_coroutines_coreDisposableHandle>
@required
- (BOOL)childCancelledCause:(DLDDSDKKotlinThrowable *)cause __attribute__((swift_name("childCancelled(cause:)")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreChildJob")))
@protocol DLDDSDKKotlinx_coroutines_coreChildJob <DLDDSDKKotlinx_coroutines_coreJob>
@required
- (void)parentCancelledParentJob:(id<DLDDSDKKotlinx_coroutines_coreParentJob>)parentJob __attribute__((swift_name("parentCancelled(parentJob:)")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreCancellationException")))
@interface DLDDSDKKotlinx_coroutines_coreCancellationException : DLDDSDKKotlinIllegalStateException
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (instancetype)initWithCause:(DLDDSDKKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinUnit")))
@interface DLDDSDKKotlinUnit : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)unit __attribute__((swift_name("init()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("KotlinSequence")))
@protocol DLDDSDKKotlinSequence
@required
- (id<DLDDSDKKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreSelectClause0")))
@protocol DLDDSDKKotlinx_coroutines_coreSelectClause0
@required
- (void)registerSelectClause0Select:(id<DLDDSDKKotlinx_coroutines_coreSelectInstance>)select block:(id<DLDDSDKKotlinSuspendFunction0>)block __attribute__((swift_name("registerSelectClause0(select:block:)")));
@end;

__attribute__((swift_name("KotlinCoroutineContextKey")))
@protocol DLDDSDKKotlinCoroutineContextKey
@required
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreFlowCollector")))
@protocol DLDDSDKKotlinx_coroutines_coreFlowCollector
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(id _Nullable)value completionHandler:(void (^)(DLDDSDKKotlinUnit * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol DLDDSDKKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<DLDDSDKKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));
- (void)encodeNullableSerializableElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<DLDDSDKKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<DLDDSDKKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) DLDDSDKKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface DLDDSDKKotlinx_serialization_coreSerializersModule : DLDDSDKBase
- (void)dumpToCollector:(id<DLDDSDKKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer> _Nullable)getContextualKclass:(id<DLDDSDKKotlinKClass>)kclass __attribute__((swift_name("getContextual(kclass:)")));
- (id<DLDDSDKKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<DLDDSDKKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));
- (id<DLDDSDKKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<DLDDSDKKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end;

__attribute__((swift_name("KotlinAnnotation")))
@protocol DLDDSDKKotlinAnnotation
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface DLDDSDKKotlinx_serialization_coreSerialKind : DLDDSDKBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol DLDDSDKKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<DLDDSDKKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<DLDDSDKKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<DLDDSDKKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<DLDDSDKKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) DLDDSDKKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface DLDDSDKKotlinNothing : DLDDSDKBase
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreParentJob")))
@protocol DLDDSDKKotlinx_coroutines_coreParentJob <DLDDSDKKotlinx_coroutines_coreJob>
@required
- (DLDDSDKKotlinx_coroutines_coreCancellationException *)getChildJobCancellationCause __attribute__((swift_name("getChildJobCancellationCause()")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreSelectInstance")))
@protocol DLDDSDKKotlinx_coroutines_coreSelectInstance
@required
- (void)disposeOnSelectHandle:(id<DLDDSDKKotlinx_coroutines_coreDisposableHandle>)handle __attribute__((swift_name("disposeOnSelect(handle:)")));
- (id _Nullable)performAtomicTrySelectDesc:(DLDDSDKKotlinx_coroutines_coreAtomicDesc *)desc __attribute__((swift_name("performAtomicTrySelect(desc:)")));
- (void)resumeSelectWithExceptionException:(DLDDSDKKotlinThrowable *)exception __attribute__((swift_name("resumeSelectWithException(exception:)")));
- (BOOL)trySelect __attribute__((swift_name("trySelect()")));
- (id _Nullable)trySelectOtherOtherOp:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp * _Nullable)otherOp __attribute__((swift_name("trySelectOther(otherOp:)")));
@property (readonly) id<DLDDSDKKotlinContinuation> completion __attribute__((swift_name("completion")));
@property (readonly) BOOL isSelected __attribute__((swift_name("isSelected")));
@end;

__attribute__((swift_name("KotlinFunction")))
@protocol DLDDSDKKotlinFunction
@required
@end;

__attribute__((swift_name("KotlinSuspendFunction0")))
@protocol DLDDSDKKotlinSuspendFunction0 <DLDDSDKKotlinFunction>
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)invokeWithCompletionHandler:(void (^)(id _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("invoke(completionHandler:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol DLDDSDKKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<DLDDSDKKotlinKClass>)kClass serializer:(id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<DLDDSDKKotlinKClass>)baseClass actualClass:(id<DLDDSDKKotlinKClass>)actualClass actualSerializer:(id<DLDDSDKKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<DLDDSDKKotlinKClass>)baseClass defaultSerializerProvider:(id<DLDDSDKKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultSerializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultSerializerProvider:)")));
@end;

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol DLDDSDKKotlinKDeclarationContainer
@required
@end;

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol DLDDSDKKotlinKAnnotatedElement
@required
@end;

__attribute__((swift_name("KotlinKClassifier")))
@protocol DLDDSDKKotlinKClassifier
@required
@end;

__attribute__((swift_name("KotlinKClass")))
@protocol DLDDSDKKotlinKClass <DLDDSDKKotlinKDeclarationContainer, DLDDSDKKotlinKAnnotatedElement, DLDDSDKKotlinKClassifier>
@required
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreAtomicDesc")))
@interface DLDDSDKKotlinx_coroutines_coreAtomicDesc : DLDDSDKBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)completeOp:(DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *)op failure:(id _Nullable)failure __attribute__((swift_name("complete(op:failure:)")));
- (id _Nullable)prepareOp:(DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *)op __attribute__((swift_name("prepare(op:)")));
@property DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *atomicOp __attribute__((swift_name("atomicOp")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreOpDescriptor")))
@interface DLDDSDKKotlinx_coroutines_coreOpDescriptor : DLDDSDKBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (BOOL)isEarlierThanThat:(DLDDSDKKotlinx_coroutines_coreOpDescriptor *)that __attribute__((swift_name("isEarlierThan(that:)")));
- (id _Nullable)performAffected:(id _Nullable)affected __attribute__((swift_name("perform(affected:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreAtomicOp<id> * _Nullable atomicOp __attribute__((swift_name("atomicOp")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_coroutines_coreLockFreeLinkedListNode.PrepareOp")))
@interface DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp : DLDDSDKKotlinx_coroutines_coreOpDescriptor
- (instancetype)initWithAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next desc:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc *)desc __attribute__((swift_name("init(affected:next:desc:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (void)finishPrepare __attribute__((swift_name("finishPrepare()")));
- (id _Nullable)performAffected:(id _Nullable)affected __attribute__((swift_name("perform(affected:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *affected __attribute__((swift_name("affected")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *atomicOp __attribute__((swift_name("atomicOp")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc *desc __attribute__((swift_name("desc")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *next __attribute__((swift_name("next")));
@end;

__attribute__((swift_name("KotlinContinuation")))
@protocol DLDDSDKKotlinContinuation
@required
- (void)resumeWithResult:(id _Nullable)result __attribute__((swift_name("resumeWith(result:)")));
@property (readonly) id<DLDDSDKKotlinCoroutineContext> context __attribute__((swift_name("context")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreAtomicOp")))
@interface DLDDSDKKotlinx_coroutines_coreAtomicOp<__contravariant T> : DLDDSDKKotlinx_coroutines_coreOpDescriptor
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)completeAffected:(T _Nullable)affected failure:(id _Nullable)failure __attribute__((swift_name("complete(affected:failure:)")));
- (id _Nullable)decideDecision:(id _Nullable)decision __attribute__((swift_name("decide(decision:)")));
- (id _Nullable)performAffected:(id _Nullable)affected __attribute__((swift_name("perform(affected:)")));
- (id _Nullable)prepareAffected:(T _Nullable)affected __attribute__((swift_name("prepare(affected:)")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *atomicOp __attribute__((swift_name("atomicOp")));
@property (readonly) id _Nullable consensus __attribute__((swift_name("consensus")));
@property (readonly) BOOL isDecided __attribute__((swift_name("isDecided")));
@property (readonly) int64_t opSequence __attribute__((swift_name("opSequence")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreLockFreeLinkedListNode")))
@interface DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode : DLDDSDKBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)addLastNode:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)node __attribute__((swift_name("addLast(node:)")));
- (BOOL)addLastIfNode:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)node condition:(DLDDSDKBoolean *(^)(void))condition __attribute__((swift_name("addLastIf(node:condition:)")));
- (BOOL)addLastIfPrevNode:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)node predicate:(DLDDSDKBoolean *(^)(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *))predicate __attribute__((swift_name("addLastIfPrev(node:predicate:)")));
- (BOOL)addLastIfPrevAndIfNode:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)node predicate:(DLDDSDKBoolean *(^)(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *))predicate condition:(DLDDSDKBoolean *(^)(void))condition __attribute__((swift_name("addLastIfPrevAndIf(node:predicate:condition:)")));
- (BOOL)addOneIfEmptyNode:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)node __attribute__((swift_name("addOneIfEmpty(node:)")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAddLastDesc<DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *> *)describeAddLastNode:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)node __attribute__((swift_name("describeAddLast(node:)")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeRemoveFirstDesc<DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *> *)describeRemoveFirst __attribute__((swift_name("describeRemoveFirst()")));
- (void)helpRemove __attribute__((swift_name("helpRemove()")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)nextIfRemoved __attribute__((swift_name("nextIfRemoved()")));
- (BOOL)remove __attribute__((swift_name("remove()")));
- (id _Nullable)removeFirstIfIsInstanceOfOrPeekIfPredicate:(DLDDSDKBoolean *(^)(id _Nullable))predicate __attribute__((swift_name("removeFirstIfIsInstanceOfOrPeekIf(predicate:)")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)removeFirstOrNull __attribute__((swift_name("removeFirstOrNull()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isRemoved __attribute__((swift_name("isRemoved")));
@property (readonly, getter=next_) id _Nullable next __attribute__((swift_name("next")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *nextNode __attribute__((swift_name("nextNode")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *prevNode __attribute__((swift_name("prevNode")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreLockFreeLinkedListNode.AbstractAtomicDesc")))
@interface DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc : DLDDSDKKotlinx_coroutines_coreAtomicDesc
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)completeOp:(DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *)op failure:(id _Nullable)failure __attribute__((swift_name("complete(op:failure:)")));
- (id _Nullable)failureAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)affected __attribute__((swift_name("failure(affected:)")));
- (void)finishOnSuccessAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next __attribute__((swift_name("finishOnSuccess(affected:next:)")));
- (void)finishPreparePrepareOp:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp *)prepareOp __attribute__((swift_name("finishPrepare(prepareOp:)")));
- (id _Nullable)onPreparePrepareOp:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp *)prepareOp __attribute__((swift_name("onPrepare(prepareOp:)")));
- (void)onRemovedAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected __attribute__((swift_name("onRemoved(affected:)")));
- (id _Nullable)prepareOp:(DLDDSDKKotlinx_coroutines_coreAtomicOp<id> *)op __attribute__((swift_name("prepare(op:)")));
- (BOOL)retryAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(id)next __attribute__((swift_name("retry(affected:next:)")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)takeAffectedNodeOp:(DLDDSDKKotlinx_coroutines_coreOpDescriptor *)op __attribute__((swift_name("takeAffectedNode(op:)")));
- (id)updatedNextAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next __attribute__((swift_name("updatedNext(affected:next:)")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable affectedNode __attribute__((swift_name("affectedNode")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable originalNext __attribute__((swift_name("originalNext")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreLockFreeLinkedListNodeAddLastDesc")))
@interface DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAddLastDesc<T> : DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc
- (instancetype)initWithQueue:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)queue node:(T)node __attribute__((swift_name("init(queue:node:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (void)finishOnSuccessAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next __attribute__((swift_name("finishOnSuccess(affected:next:)")));
- (void)finishPreparePrepareOp:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp *)prepareOp __attribute__((swift_name("finishPrepare(prepareOp:)")));
- (BOOL)retryAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(id)next __attribute__((swift_name("retry(affected:next:)")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)takeAffectedNodeOp:(DLDDSDKKotlinx_coroutines_coreOpDescriptor *)op __attribute__((swift_name("takeAffectedNode(op:)")));
- (id)updatedNextAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next __attribute__((swift_name("updatedNext(affected:next:)")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable affectedNode __attribute__((swift_name("affectedNode")));
@property (readonly) T node __attribute__((swift_name("node")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *originalNext __attribute__((swift_name("originalNext")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *queue __attribute__((swift_name("queue")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreLockFreeLinkedListNodeRemoveFirstDesc")))
@interface DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeRemoveFirstDesc<T> : DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc
- (instancetype)initWithQueue:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)queue __attribute__((swift_name("init(queue:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (id _Nullable)failureAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)affected __attribute__((swift_name("failure(affected:)")));
- (void)finishOnSuccessAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next __attribute__((swift_name("finishOnSuccess(affected:next:)")));
- (void)finishPreparePrepareOp:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp *)prepareOp __attribute__((swift_name("finishPrepare(prepareOp:)")));
- (BOOL)retryAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(id)next __attribute__((swift_name("retry(affected:next:)")));
- (DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable)takeAffectedNodeOp:(DLDDSDKKotlinx_coroutines_coreOpDescriptor *)op __attribute__((swift_name("takeAffectedNode(op:)")));
- (id)updatedNextAffected:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)affected next:(DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *)next __attribute__((swift_name("updatedNext(affected:next:)")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable affectedNode __attribute__((swift_name("affectedNode")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode * _Nullable originalNext __attribute__((swift_name("originalNext")));
@property (readonly) DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode *queue __attribute__((swift_name("queue")));
@property (readonly) T _Nullable result __attribute__((swift_name("result")));
@end;

#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
