#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class DLDDSDKDataDonationSDKPublicAPIEnvironment, DLDDSDKKotlinEnum<E>, DLDDSDKKotlinArray<T>, DLDDSDKKotlinException, DLDDSDKConsentCreationPayload, DLDDSDKConsentMessage, DLDDSDKConsentRequest, DLDDSDKConsentRevocationPayload, DLDDSDKConsentSignature, DLDDSDKConsentSignatureType, DLDDSDKConsentSigningRequest, DLDDSDKKotlinByteArray, DLDDSDKDocumentWithSignature, DLDDSDKDonationPayload, DLDDSDKSignedConsentMessage, DLDDSDKTokenVerificationResult, DLDDSDKConsentDocument, DLDDSDKModelContractConsentEvent, DLDDSDKUserConsent, DLDDSDKKotlinThrowable, DLDDSDKKotlinRuntimeException, DLDDSDKUtilD4LRuntimeException, DLDDSDKConsentServiceError, DLDDSDKCoreRuntimeError, DLDDSDKKoin_coreModule, DLDDSDKKotlinByteIterator, DLDDSDKKotlinx_datetimeInstant, DLDDSDKKotlinIllegalStateException, DLDDSDKKoin_coreBeanDefinition<T>, DLDDSDKKoin_coreScope, DLDDSDKKoin_coreDefinitionParameters, DLDDSDKKoin_coreOptions, DLDDSDKKoin_coreScopeDSL, DLDDSDKKotlinx_coroutines_coreCancellationException, DLDDSDKKotlinUnit, DLDDSDKKotlinx_serialization_coreSerializersModule, DLDDSDKKotlinx_serialization_coreSerialKind, DLDDSDKKotlinNothing, DLDDSDKKoin_coreKind, DLDDSDKKoin_coreProperties, DLDDSDKKoin_coreCallbacks<T>, DLDDSDKKoin_coreScopeDefinition, DLDDSDKKoin_coreKoin, DLDDSDKKotlinLazyThreadSafetyMode, DLDDSDKKoin_coreLogger, DLDDSDKKoin_corePropertyRegistry, DLDDSDKKoin_coreScopeRegistry, DLDDSDKKoin_coreLevel, DLDDSDKKotlinx_coroutines_coreAtomicDesc, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodePrepareOp, DLDDSDKKotlinx_coroutines_coreAtomicOp<__contravariant T>, DLDDSDKKotlinx_coroutines_coreOpDescriptor, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNode, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAbstractAtomicDesc, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeAddLastDesc<T>, DLDDSDKKotlinx_coroutines_coreLockFreeLinkedListNodeRemoveFirstDesc<T>;

@protocol DLDDSDKUtil_coroutineD4LSDKFlowContract, DLDDSDKDataDonationSDKPublicAPIDataDonationClient, DLDDSDKDataDonationSDKPublicAPIUserSessionTokenProvider, DLDDSDKDataDonationSDKPublicAPIDataDonationClientFactory, DLDDSDKKotlinComparable, DLDDSDKKotlinx_serialization_coreKSerializer, DLDDSDKKotlinx_datetimeClock, DLDDSDKUsecaseContractUsecase, DLDDSDKModelContractConsentDocument, DLDDSDKModelContractUserConsent, DLDDSDKKotlinx_coroutines_coreJob, DLDDSDKKotlinx_coroutines_coreCoroutineScope, DLDDSDKKotlinx_coroutines_coreFlow, DLDDSDKKotlinIterator, DLDDSDKKotlinx_serialization_coreEncoder, DLDDSDKKotlinx_serialization_coreSerialDescriptor, DLDDSDKKotlinx_serialization_coreSerializationStrategy, DLDDSDKKotlinx_serialization_coreDecoder, DLDDSDKKotlinx_serialization_coreDeserializationStrategy, DLDDSDKKoin_coreQualifier, DLDDSDKKotlinx_coroutines_coreChildHandle, DLDDSDKKotlinx_coroutines_coreChildJob, DLDDSDKKotlinx_coroutines_coreDisposableHandle, DLDDSDKKotlinSequence, DLDDSDKKotlinx_coroutines_coreSelectClause0, DLDDSDKKotlinCoroutineContextKey, DLDDSDKKotlinCoroutineContextElement, DLDDSDKKotlinCoroutineContext, DLDDSDKKotlinx_coroutines_coreFlowCollector, DLDDSDKKotlinx_serialization_coreCompositeEncoder, DLDDSDKKotlinAnnotation, DLDDSDKKotlinx_serialization_coreCompositeDecoder, DLDDSDKKotlinKClass, DLDDSDKKotlinLazy, DLDDSDKKoin_coreScopeCallback, DLDDSDKKotlinx_coroutines_coreParentJob, DLDDSDKKotlinx_coroutines_coreSelectInstance, DLDDSDKKotlinSuspendFunction0, DLDDSDKKotlinx_serialization_coreSerializersModuleCollector, DLDDSDKKotlinKDeclarationContainer, DLDDSDKKotlinKAnnotatedElement, DLDDSDKKotlinKClassifier, DLDDSDKKoin_coreKoinScopeComponent, DLDDSDKKotlinContinuation, DLDDSDKKotlinFunction, DLDDSDKKoin_coreKoinComponent;

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

__attribute__((swift_name("DataDonationSDKPublicAPI")))
@protocol DLDDSDKDataDonationSDKPublicAPI
@required
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
__attribute__((swift_name("ConsentCreationPayload")))
@interface DLDDSDKConsentCreationPayload : DLDDSDKBase
- (instancetype)initWithConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(int32_t)consentDocumentVersion consentDate:(NSString *)consentDate __attribute__((swift_name("init(consentDocumentKey:consentDocumentVersion:consentDate:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (int32_t)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKConsentCreationPayload *)doCopyConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(int32_t)consentDocumentVersion consentDate:(NSString *)consentDate __attribute__((swift_name("doCopy(consentDocumentKey:consentDocumentVersion:consentDate:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *consentDate __attribute__((swift_name("consentDate")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) int32_t consentDocumentVersion __attribute__((swift_name("consentDocumentVersion")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentCreationPayload.Companion")))
@interface DLDDSDKConsentCreationPayloadCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentMessage")))
@interface DLDDSDKConsentMessage : DLDDSDKBase
- (instancetype)initWithConsentDocumentKey:(NSString *)consentDocumentKey signatureType:(NSString *)signatureType payload:(NSString *)payload __attribute__((swift_name("init(consentDocumentKey:signatureType:payload:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKConsentMessage *)doCopyConsentDocumentKey:(NSString *)consentDocumentKey signatureType:(NSString *)signatureType payload:(NSString *)payload __attribute__((swift_name("doCopy(consentDocumentKey:signatureType:payload:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString *payload __attribute__((swift_name("payload")));
@property (readonly) NSString *signatureType __attribute__((swift_name("signatureType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentMessage.Companion")))
@interface DLDDSDKConsentMessageCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentRequest")))
@interface DLDDSDKConsentRequest : DLDDSDKBase
- (instancetype)initWithDonorId:(NSString *)donorId token:(NSString *)token __attribute__((swift_name("init(donorId:token:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (DLDDSDKConsentRequest *)doCopyDonorId:(NSString *)donorId token:(NSString *)token __attribute__((swift_name("doCopy(donorId:token:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *donorId __attribute__((swift_name("donorId")));
@property (readonly) NSString *token __attribute__((swift_name("token")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentRequest.Companion")))
@interface DLDDSDKConsentRequestCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentRevocationPayload")))
@interface DLDDSDKConsentRevocationPayload : DLDDSDKBase
- (instancetype)initWithConsentDocumentKey:(NSString *)consentDocumentKey __attribute__((swift_name("init(consentDocumentKey:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (DLDDSDKConsentRevocationPayload *)doCopyConsentDocumentKey:(NSString *)consentDocumentKey __attribute__((swift_name("doCopy(consentDocumentKey:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentRevocationPayload.Companion")))
@interface DLDDSDKConsentRevocationPayloadCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentSignature")))
@interface DLDDSDKConsentSignature : DLDDSDKBase
- (instancetype)initWithSignature:(NSString *)signature __attribute__((swift_name("init(signature:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (DLDDSDKConsentSignature *)doCopySignature:(NSString *)signature __attribute__((swift_name("doCopy(signature:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *signature __attribute__((swift_name("signature")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentSignature.Companion")))
@interface DLDDSDKConsentSignatureCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentSignatureType")))
@interface DLDDSDKConsentSignatureType : DLDDSDKKotlinEnum<DLDDSDKConsentSignatureType *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKConsentSignatureType *consentOnce __attribute__((swift_name("consentOnce")));
@property (class, readonly) DLDDSDKConsentSignatureType *normalUse __attribute__((swift_name("normalUse")));
@property (class, readonly) DLDDSDKConsentSignatureType *revokeOnce __attribute__((swift_name("revokeOnce")));
+ (DLDDSDKKotlinArray<DLDDSDKConsentSignatureType *> *)values __attribute__((swift_name("values()")));
@property (readonly) NSString *apiValue __attribute__((swift_name("apiValue")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentSignatureType.Companion")))
@interface DLDDSDKConsentSignatureTypeCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentSigningRequest")))
@interface DLDDSDKConsentSigningRequest : DLDDSDKBase
- (instancetype)initWithConsentDocumentKey:(NSString *)consentDocumentKey payload:(NSString *)payload signatureType:(NSString *)signatureType __attribute__((swift_name("init(consentDocumentKey:payload:signatureType:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKConsentSigningRequest *)doCopyConsentDocumentKey:(NSString *)consentDocumentKey payload:(NSString *)payload signatureType:(NSString *)signatureType __attribute__((swift_name("doCopy(consentDocumentKey:payload:signatureType:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString *payload __attribute__((swift_name("payload")));
@property (readonly) NSString *signatureType __attribute__((swift_name("signatureType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentSigningRequest.Companion")))
@interface DLDDSDKConsentSigningRequestCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DocumentWithSignature")))
@interface DLDDSDKDocumentWithSignature : DLDDSDKBase
- (instancetype)initWithDocument:(DLDDSDKKotlinByteArray *)document signature:(DLDDSDKKotlinByteArray *)signature __attribute__((swift_name("init(document:signature:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKotlinByteArray *)component1 __attribute__((swift_name("component1()")));
- (DLDDSDKKotlinByteArray *)component2 __attribute__((swift_name("component2()")));
- (DLDDSDKDocumentWithSignature *)doCopyDocument:(DLDDSDKKotlinByteArray *)document signature:(DLDDSDKKotlinByteArray *)signature __attribute__((swift_name("doCopy(document:signature:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) DLDDSDKKotlinByteArray *document __attribute__((swift_name("document")));
@property (readonly) DLDDSDKKotlinByteArray *signature __attribute__((swift_name("signature")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DonationPayload")))
@interface DLDDSDKDonationPayload : DLDDSDKBase
- (instancetype)initWithRequest:(DLDDSDKKotlinByteArray *)request documents:(NSArray<DLDDSDKDocumentWithSignature *> *)documents __attribute__((swift_name("init(request:documents:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKotlinByteArray *)component1 __attribute__((swift_name("component1()")));
- (NSArray<DLDDSDKDocumentWithSignature *> *)component2 __attribute__((swift_name("component2()")));
- (DLDDSDKDonationPayload *)doCopyRequest:(DLDDSDKKotlinByteArray *)request documents:(NSArray<DLDDSDKDocumentWithSignature *> *)documents __attribute__((swift_name("doCopy(request:documents:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<DLDDSDKDocumentWithSignature *> *documents __attribute__((swift_name("documents")));
@property (readonly) DLDDSDKKotlinByteArray *request __attribute__((swift_name("request")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SignedConsentMessage")))
@interface DLDDSDKSignedConsentMessage : DLDDSDKBase
- (instancetype)initWithConsentMessageJson:(NSString *)consentMessageJson signature:(NSString *)signature __attribute__((swift_name("init(consentMessageJson:signature:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (DLDDSDKSignedConsentMessage *)doCopyConsentMessageJson:(NSString *)consentMessageJson signature:(NSString *)signature __attribute__((swift_name("doCopy(consentMessageJson:signature:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *consentMessageJson __attribute__((swift_name("consentMessageJson")));
@property (readonly) NSString *signature __attribute__((swift_name("signature")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SignedConsentMessage.Companion")))
@interface DLDDSDKSignedConsentMessageCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TokenVerificationResult")))
@interface DLDDSDKTokenVerificationResult : DLDDSDKBase
- (instancetype)initWithStudyId:(NSString * _Nullable)studyId externalId:(NSString * _Nullable)externalId errors:(NSString * _Nullable)errors __attribute__((swift_name("init(studyId:externalId:errors:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKTokenVerificationResult *)doCopyStudyId:(NSString * _Nullable)studyId externalId:(NSString * _Nullable)externalId errors:(NSString * _Nullable)errors __attribute__((swift_name("doCopy(studyId:externalId:errors:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable errors __attribute__((swift_name("errors")));
@property (readonly) NSString * _Nullable externalId __attribute__((swift_name("externalId")));
@property (readonly) NSString * _Nullable studyId __attribute__((swift_name("studyId")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TokenVerificationResult.Companion")))
@interface DLDDSDKTokenVerificationResultCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CachedUserSessionTokenService")))
@interface DLDDSDKCachedUserSessionTokenService : DLDDSDKBase
- (instancetype)initWithProvider:(id<DLDDSDKDataDonationSDKPublicAPIUserSessionTokenProvider>)provider clock:(id<DLDDSDKKotlinx_datetimeClock>)clock __attribute__((swift_name("init(provider:clock:)"))) __attribute__((objc_designated_initializer));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)getUserSessionTokenWithCompletionHandler:(void (^)(NSString * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("getUserSessionToken(completionHandler:)")));
@end;

__attribute__((swift_name("UsecaseContract")))
@protocol DLDDSDKUsecaseContract
@required
@end;

__attribute__((swift_name("UsecaseContractUsecase")))
@protocol DLDDSDKUsecaseContractUsecase
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)executeParameter:(id)parameter completionHandler:(void (^)(id _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("execute(parameter:completionHandler:)")));
@end;

__attribute__((swift_name("UsecaseContractCreateUserConsent")))
@protocol DLDDSDKUsecaseContractCreateUserConsent <DLDDSDKUsecaseContractUsecase>
@required
@end;

__attribute__((swift_name("UsecaseContractCreateUserConsentParameter")))
@protocol DLDDSDKUsecaseContractCreateUserConsentParameter
@required
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end;

__attribute__((swift_name("UsecaseContractFetchConsentDocuments")))
@protocol DLDDSDKUsecaseContractFetchConsentDocuments <DLDDSDKUsecaseContractUsecase>
@required
@end;

__attribute__((swift_name("UsecaseContractFetchConsentDocumentsParameter")))
@protocol DLDDSDKUsecaseContractFetchConsentDocumentsParameter
@required
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) DLDDSDKInt * _Nullable version_ __attribute__((swift_name("version_")));
@end;

__attribute__((swift_name("UsecaseContractFetchUserConsents")))
@protocol DLDDSDKUsecaseContractFetchUserConsents <DLDDSDKUsecaseContractUsecase>
@required
@end;

__attribute__((swift_name("UsecaseContractFetchUserConsentsParameter")))
@protocol DLDDSDKUsecaseContractFetchUserConsentsParameter
@required
@property (readonly) NSString * _Nullable consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@end;

__attribute__((swift_name("UsecaseContractRedactSensitiveInformation")))
@protocol DLDDSDKUsecaseContractRedactSensitiveInformation <DLDDSDKUsecaseContractUsecase>
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UsecaseContractRedactSensitiveInformationCompanion")))
@interface DLDDSDKUsecaseContractRedactSensitiveInformationCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (readonly) NSString *REDACTED __attribute__((swift_name("REDACTED")));
@end;

__attribute__((swift_name("UsecaseContractRevokeUserConsent")))
@protocol DLDDSDKUsecaseContractRevokeUserConsent <DLDDSDKUsecaseContractUsecase>
@required
@end;

__attribute__((swift_name("UsecaseContractRevokeUserConsentParameter")))
@protocol DLDDSDKUsecaseContractRevokeUserConsentParameter
@required
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@end;

__attribute__((swift_name("ModelContractConsentDocument")))
@protocol DLDDSDKModelContractConsentDocument
@required
@property (readonly) NSString *description_ __attribute__((swift_name("description_")));
@property (readonly) NSString *key __attribute__((swift_name("key")));
@property (readonly) NSString *language __attribute__((swift_name("language")));
@property (readonly) NSString *processor __attribute__((swift_name("processor")));
@property (readonly) NSString *programName __attribute__((swift_name("programName")));
@property (readonly) NSString *recipient __attribute__((swift_name("recipient")));
@property (readonly) BOOL requiresToken __attribute__((swift_name("requiresToken")));
@property (readonly) NSString *studyId __attribute__((swift_name("studyId")));
@property (readonly) NSString *text __attribute__((swift_name("text")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConsentDocument")))
@interface DLDDSDKConsentDocument : DLDDSDKBase <DLDDSDKModelContractConsentDocument>
- (instancetype)initWithKey:(NSString *)key version:(int32_t)version processor:(NSString *)processor description:(NSString *)description recipient:(NSString *)recipient language:(NSString *)language text:(NSString *)text requiresToken:(BOOL)requiresToken studyId:(NSString *)studyId programName:(NSString *)programName __attribute__((swift_name("init(key:version:processor:description:recipient:language:text:requiresToken:studyId:programName:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component10 __attribute__((swift_name("component10()")));
- (int32_t)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString *)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (NSString *)component6 __attribute__((swift_name("component6()")));
- (NSString *)component7 __attribute__((swift_name("component7()")));
- (BOOL)component8 __attribute__((swift_name("component8()")));
- (NSString *)component9 __attribute__((swift_name("component9()")));
- (DLDDSDKConsentDocument *)doCopyKey:(NSString *)key version:(int32_t)version processor:(NSString *)processor description:(NSString *)description recipient:(NSString *)recipient language:(NSString *)language text:(NSString *)text requiresToken:(BOOL)requiresToken studyId:(NSString *)studyId programName:(NSString *)programName __attribute__((swift_name("doCopy(key:version:processor:description:recipient:language:text:requiresToken:studyId:programName:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *description_ __attribute__((swift_name("description_")));
@property (readonly) NSString *key __attribute__((swift_name("key")));
@property (readonly) NSString *language __attribute__((swift_name("language")));
@property (readonly) NSString *processor __attribute__((swift_name("processor")));
@property (readonly) NSString *programName __attribute__((swift_name("programName")));
@property (readonly) NSString *recipient __attribute__((swift_name("recipient")));
@property (readonly) BOOL requiresToken __attribute__((swift_name("requiresToken")));
@property (readonly) NSString *studyId __attribute__((swift_name("studyId")));
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

__attribute__((swift_name("ModelContract")))
@protocol DLDDSDKModelContract
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ModelContractConsentEvent")))
@interface DLDDSDKModelContractConsentEvent : DLDDSDKKotlinEnum<DLDDSDKModelContractConsentEvent *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKModelContractConsentEvent *consent __attribute__((swift_name("consent")));
@property (class, readonly) DLDDSDKModelContractConsentEvent *revoke __attribute__((swift_name("revoke")));
+ (DLDDSDKKotlinArray<DLDDSDKModelContractConsentEvent *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ModelContractConsentEvent.Companion")))
@interface DLDDSDKModelContractConsentEventCompanion : DLDDSDKBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<DLDDSDKKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("ModelContractKeyPair")))
@protocol DLDDSDKModelContractKeyPair
@required
@property (readonly, getter=private) DLDDSDKKotlinByteArray *private_ __attribute__((swift_name("private_")));
@property (readonly, getter=public) DLDDSDKKotlinByteArray *public_ __attribute__((swift_name("public_")));
@end;

__attribute__((swift_name("ModelContractUserConsent")))
@protocol DLDDSDKModelContractUserConsent
@required
@property (readonly) NSString *accountId __attribute__((swift_name("accountId")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString *consentDocumentVersion __attribute__((swift_name("consentDocumentVersion")));
@property (readonly) NSString *createdAt __attribute__((swift_name("createdAt")));
@property (readonly) DLDDSDKModelContractConsentEvent *event __attribute__((swift_name("event")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserConsent")))
@interface DLDDSDKUserConsent : DLDDSDKBase <DLDDSDKModelContractUserConsent>
- (instancetype)initWithConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(NSString *)consentDocumentVersion accountId:(NSString *)accountId event:(DLDDSDKModelContractConsentEvent *)event createdAt:(NSString *)createdAt __attribute__((swift_name("init(consentDocumentKey:consentDocumentVersion:accountId:event:createdAt:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKModelContractConsentEvent *)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (DLDDSDKUserConsent *)doCopyConsentDocumentKey:(NSString *)consentDocumentKey consentDocumentVersion:(NSString *)consentDocumentVersion accountId:(NSString *)accountId event:(DLDDSDKModelContractConsentEvent *)event createdAt:(NSString *)createdAt __attribute__((swift_name("doCopy(consentDocumentKey:consentDocumentVersion:accountId:event:createdAt:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *accountId __attribute__((swift_name("accountId")));
@property (readonly) NSString *consentDocumentKey __attribute__((swift_name("consentDocumentKey")));
@property (readonly) NSString *consentDocumentVersion __attribute__((swift_name("consentDocumentVersion")));
@property (readonly) NSString *createdAt __attribute__((swift_name("createdAt")));
@property (readonly) DLDDSDKModelContractConsentEvent *event __attribute__((swift_name("event")));
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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PluginKoinKt")))
@interface DLDDSDKPluginKoinKt : DLDDSDKBase
+ (DLDDSDKKoin_coreModule *)resolveKtorPlugins __attribute__((swift_name("resolveKtorPlugins()")));
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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface DLDDSDKKotlinByteArray : DLDDSDKBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(DLDDSDKByte *(^)(DLDDSDKInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (DLDDSDKKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("Kotlinx_datetimeClock")))
@protocol DLDDSDKKotlinx_datetimeClock
@required
- (DLDDSDKKotlinx_datetimeInstant *)now __attribute__((swift_name("now()")));
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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreModule")))
@interface DLDDSDKKoin_coreModule : DLDDSDKBase
- (instancetype)initWithCreateAtStart:(BOOL)createAtStart override:(BOOL)override __attribute__((swift_name("init(createAtStart:override:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKoin_coreBeanDefinition<id> *)factoryQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier override:(BOOL)override definition:(id _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition __attribute__((swift_name("factory(qualifier:override:definition:)")));
- (DLDDSDKKoin_coreOptions *)makeOptionsOverride:(BOOL)override createdAtStart:(BOOL)createdAtStart __attribute__((swift_name("makeOptions(override:createdAtStart:)")));
- (NSArray<DLDDSDKKoin_coreModule *> *)plusModules:(NSArray<DLDDSDKKoin_coreModule *> *)modules __attribute__((swift_name("plus(modules:)")));
- (NSArray<DLDDSDKKoin_coreModule *> *)plusModule:(DLDDSDKKoin_coreModule *)module __attribute__((swift_name("plus(module:)")));
- (void)scopeQualifier:(id<DLDDSDKKoin_coreQualifier>)qualifier scopeSet:(void (^)(DLDDSDKKoin_coreScopeDSL *))scopeSet __attribute__((swift_name("scope(qualifier:scopeSet:)")));
- (void)scopeScopeSet:(void (^)(DLDDSDKKoin_coreScopeDSL *))scopeSet __attribute__((swift_name("scope(scopeSet:)")));
- (DLDDSDKKoin_coreBeanDefinition<id> *)singleQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier createdAtStart:(BOOL)createdAtStart override:(BOOL)override definition:(id _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition __attribute__((swift_name("single(qualifier:createdAtStart:override:definition:)")));
@property (readonly) BOOL isLoaded __attribute__((swift_name("isLoaded")));
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

__attribute__((swift_name("KotlinByteIterator")))
@interface DLDDSDKKotlinByteIterator : DLDDSDKBase <DLDDSDKKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (DLDDSDKByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeInstant")))
@interface DLDDSDKKotlinx_datetimeInstant : DLDDSDKBase <DLDDSDKKotlinComparable>
- (int32_t)compareToOther:(DLDDSDKKotlinx_datetimeInstant *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (DLDDSDKKotlinx_datetimeInstant *)minusDuration:(double)duration __attribute__((swift_name("minus(duration:)")));
- (double)minusOther:(DLDDSDKKotlinx_datetimeInstant *)other __attribute__((swift_name("minus(other:)")));
- (DLDDSDKKotlinx_datetimeInstant *)plusDuration:(double)duration __attribute__((swift_name("plus(duration:)")));
- (int64_t)toEpochMilliseconds __attribute__((swift_name("toEpochMilliseconds()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t epochSeconds __attribute__((swift_name("epochSeconds")));
@property (readonly) int32_t nanosecondsOfSecond __attribute__((swift_name("nanosecondsOfSecond")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreBeanDefinition")))
@interface DLDDSDKKoin_coreBeanDefinition<T> : DLDDSDKBase
- (instancetype)initWithScopeQualifier:(id<DLDDSDKKoin_coreQualifier>)scopeQualifier primaryType:(id<DLDDSDKKotlinKClass>)primaryType qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier definition:(T _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition kind:(DLDDSDKKoin_coreKind *)kind secondaryTypes:(NSArray<id<DLDDSDKKotlinKClass>> *)secondaryTypes options:(DLDDSDKKoin_coreOptions *)options properties:(DLDDSDKKoin_coreProperties *)properties __attribute__((swift_name("init(scopeQualifier:primaryType:qualifier:definition:kind:secondaryTypes:options:properties:)"))) __attribute__((objc_designated_initializer));
- (BOOL)canBindPrimary:(id<DLDDSDKKotlinKClass>)primary secondary:(id<DLDDSDKKotlinKClass>)secondary __attribute__((swift_name("canBind(primary:secondary:)")));
- (id<DLDDSDKKoin_coreQualifier>)component1 __attribute__((swift_name("component1()")));
- (id<DLDDSDKKotlinKClass>)component2 __attribute__((swift_name("component2()")));
- (id<DLDDSDKKoin_coreQualifier> _Nullable)component3 __attribute__((swift_name("component3()")));
- (T _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))component4 __attribute__((swift_name("component4()")));
- (DLDDSDKKoin_coreKind *)component5 __attribute__((swift_name("component5()")));
- (NSArray<id<DLDDSDKKotlinKClass>> *)component6 __attribute__((swift_name("component6()")));
- (DLDDSDKKoin_coreOptions *)component7 __attribute__((swift_name("component7()")));
- (DLDDSDKKoin_coreProperties *)component8 __attribute__((swift_name("component8()")));
- (DLDDSDKKoin_coreBeanDefinition<T> *)doCopyScopeQualifier:(id<DLDDSDKKoin_coreQualifier>)scopeQualifier primaryType:(id<DLDDSDKKotlinKClass>)primaryType qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier definition:(T _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition kind:(DLDDSDKKoin_coreKind *)kind secondaryTypes:(NSArray<id<DLDDSDKKotlinKClass>> *)secondaryTypes options:(DLDDSDKKoin_coreOptions *)options properties:(DLDDSDKKoin_coreProperties *)properties __attribute__((swift_name("doCopy(scopeQualifier:primaryType:qualifier:definition:kind:secondaryTypes:options:properties:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (BOOL)hasTypeClazz:(id<DLDDSDKKotlinKClass>)clazz __attribute__((swift_name("hasType(clazz:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isClazz:(id<DLDDSDKKotlinKClass>)clazz qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier scopeDefinition:(id<DLDDSDKKoin_coreQualifier>)scopeDefinition __attribute__((swift_name("is(clazz:qualifier:scopeDefinition:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property DLDDSDKKoin_coreCallbacks<T> *callbacks __attribute__((swift_name("callbacks")));
@property (readonly) T _Nullable (^definition)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *) __attribute__((swift_name("definition")));
@property (readonly) DLDDSDKKoin_coreKind *kind __attribute__((swift_name("kind")));
@property (readonly) DLDDSDKKoin_coreOptions *options __attribute__((swift_name("options")));
@property (readonly) id<DLDDSDKKotlinKClass> primaryType __attribute__((swift_name("primaryType")));
@property (readonly) DLDDSDKKoin_coreProperties *properties __attribute__((swift_name("properties")));
@property (readonly) id<DLDDSDKKoin_coreQualifier> _Nullable qualifier __attribute__((swift_name("qualifier")));
@property (readonly) id<DLDDSDKKoin_coreQualifier> scopeQualifier __attribute__((swift_name("scopeQualifier")));
@property NSArray<id<DLDDSDKKotlinKClass>> *secondaryTypes __attribute__((swift_name("secondaryTypes")));
@end;

__attribute__((swift_name("Koin_coreQualifier")))
@protocol DLDDSDKKoin_coreQualifier
@required
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreScope")))
@interface DLDDSDKKoin_coreScope : DLDDSDKBase
- (instancetype)initWithId:(NSString *)id _scopeDefinition:(DLDDSDKKoin_coreScopeDefinition *)_scopeDefinition _koin:(DLDDSDKKoin_coreKoin *)_koin __attribute__((swift_name("init(id:_scopeDefinition:_koin:)"))) __attribute__((objc_designated_initializer));
- (void)addParametersParameters:(DLDDSDKKoin_coreDefinitionParameters *)parameters __attribute__((swift_name("addParameters(parameters:)")));
- (id _Nullable)bindPrimaryType:(id<DLDDSDKKotlinKClass>)primaryType secondaryType:(id<DLDDSDKKotlinKClass>)secondaryType parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("bind(primaryType:secondaryType:parameters:)")));
- (id _Nullable)bindParameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("bind(parameters:)")));
- (void)clearParameters __attribute__((swift_name("clearParameters()")));
- (void)close __attribute__((swift_name("close()")));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (DLDDSDKKoin_coreScopeDefinition *)component2 __attribute__((swift_name("component2()")));
- (DLDDSDKKoin_coreScope *)doCopyId:(NSString *)id _scopeDefinition:(DLDDSDKKoin_coreScopeDefinition *)_scopeDefinition _koin:(DLDDSDKKoin_coreKoin *)_koin __attribute__((swift_name("doCopy(id:_scopeDefinition:_koin:)")));
- (void)declareInstance:(id _Nullable)instance qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier secondaryTypes:(NSArray<id<DLDDSDKKotlinKClass>> * _Nullable)secondaryTypes override:(BOOL)override __attribute__((swift_name("declare(instance:qualifier:secondaryTypes:override:)")));
- (void)dropInstanceBeanDefinition:(DLDDSDKKoin_coreBeanDefinition<id> *)beanDefinition __attribute__((swift_name("dropInstance(beanDefinition:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (id _Nullable)getClazz:(id<DLDDSDKKotlinKClass>)clazz qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("get(clazz:qualifier:parameters:)")));
- (id)getQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("get(qualifier:parameters:)")));
- (NSArray<id> *)getAll __attribute__((swift_name("getAll()")));
- (NSArray<id> *)getAllClazz:(id<DLDDSDKKotlinKClass>)clazz __attribute__((swift_name("getAll(clazz:)")));
- (DLDDSDKKoin_coreKoin *)getKoin __attribute__((swift_name("getKoin()")));
- (id _Nullable)getOrNullClazz:(id<DLDDSDKKotlinKClass>)clazz qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("getOrNull(clazz:qualifier:parameters:)")));
- (id _Nullable)getOrNullQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("getOrNull(qualifier:parameters:)")));
- (NSString *)getPropertyKey:(NSString *)key __attribute__((swift_name("getProperty(key:)")));
- (NSString *)getPropertyKey:(NSString *)key defaultValue:(NSString *)defaultValue __attribute__((swift_name("getProperty(key:defaultValue:)")));
- (NSString * _Nullable)getPropertyOrNullKey:(NSString *)key __attribute__((swift_name("getPropertyOrNull(key:)")));
- (DLDDSDKKoin_coreScope *)getScopeScopeID:(NSString *)scopeID __attribute__((swift_name("getScope(scopeID:)")));
- (id)getSource __attribute__((swift_name("getSource()")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (id<DLDDSDKKotlinLazy>)injectQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier mode:(DLDDSDKKotlinLazyThreadSafetyMode *)mode parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("inject(qualifier:mode:parameters:)")));
- (id<DLDDSDKKotlinLazy>)injectOrNullQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier mode:(DLDDSDKKotlinLazyThreadSafetyMode *)mode parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("injectOrNull(qualifier:mode:parameters:)")));
- (BOOL)isNotClosed __attribute__((swift_name("isNotClosed()")));
- (void)linkToScopes:(DLDDSDKKotlinArray<DLDDSDKKoin_coreScope *> *)scopes __attribute__((swift_name("linkTo(scopes:)")));
- (void)loadDefinitionBeanDefinition:(DLDDSDKKoin_coreBeanDefinition<id> *)beanDefinition __attribute__((swift_name("loadDefinition(beanDefinition:)")));
- (void)registerCallbackCallback:(id<DLDDSDKKoin_coreScopeCallback>)callback __attribute__((swift_name("registerCallback(callback:)")));
- (void)setSourceT:(id _Nullable)t __attribute__((swift_name("setSource(t:)")));
- (NSString *)description __attribute__((swift_name("description()")));
- (void)unlinkScopes:(DLDDSDKKotlinArray<DLDDSDKKoin_coreScope *> *)scopes __attribute__((swift_name("unlink(scopes:)")));
@property (readonly) DLDDSDKKoin_coreScopeDefinition *_scopeDefinition __attribute__((swift_name("_scopeDefinition")));
@property (readonly) BOOL closed __attribute__((swift_name("closed")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) DLDDSDKKoin_coreLogger *logger __attribute__((swift_name("logger")));
@end;

__attribute__((swift_name("Koin_coreDefinitionParameters")))
@interface DLDDSDKKoin_coreDefinitionParameters : DLDDSDKBase
- (instancetype)initWithValues:(NSArray<id> *)values __attribute__((swift_name("init(values:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKoin_coreDefinitionParameters *)addValue:(id)value __attribute__((swift_name("add(value:)")));
- (id _Nullable)component1 __attribute__((swift_name("component1()")));
- (id _Nullable)component2 __attribute__((swift_name("component2()")));
- (id _Nullable)component3 __attribute__((swift_name("component3()")));
- (id _Nullable)component4 __attribute__((swift_name("component4()")));
- (id _Nullable)component5 __attribute__((swift_name("component5()")));
- (id _Nullable)elementAtI:(int32_t)i clazz:(id<DLDDSDKKotlinKClass>)clazz __attribute__((swift_name("elementAt(i:clazz:)")));
- (id)get __attribute__((swift_name("get()")));
- (id _Nullable)getI:(int32_t)i __attribute__((swift_name("get(i:)")));
- (id _Nullable)getOrNullClazz:(id<DLDDSDKKotlinKClass>)clazz __attribute__((swift_name("getOrNull(clazz:)")));
- (DLDDSDKKoin_coreDefinitionParameters *)insertIndex:(int32_t)index value:(id)value __attribute__((swift_name("insert(index:value:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (BOOL)isNotEmpty __attribute__((swift_name("isNotEmpty()")));
- (void)setI:(int32_t)i t:(id _Nullable)t __attribute__((swift_name("set(i:t:)")));
- (int32_t)size __attribute__((swift_name("size()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id> *values __attribute__((swift_name("values")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreOptions")))
@interface DLDDSDKKoin_coreOptions : DLDDSDKBase
- (instancetype)initWithIsCreatedAtStart:(BOOL)isCreatedAtStart override:(BOOL)override isExtraDefinition:(BOOL)isExtraDefinition __attribute__((swift_name("init(isCreatedAtStart:override:isExtraDefinition:)"))) __attribute__((objc_designated_initializer));
- (BOOL)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (DLDDSDKKoin_coreOptions *)doCopyIsCreatedAtStart:(BOOL)isCreatedAtStart override:(BOOL)override isExtraDefinition:(BOOL)isExtraDefinition __attribute__((swift_name("doCopy(isCreatedAtStart:override:isExtraDefinition:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property BOOL isCreatedAtStart __attribute__((swift_name("isCreatedAtStart")));
@property BOOL isExtraDefinition __attribute__((swift_name("isExtraDefinition")));
@property BOOL override __attribute__((swift_name("override")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreScopeDSL")))
@interface DLDDSDKKoin_coreScopeDSL : DLDDSDKBase
- (instancetype)initWithScopeQualifier:(id<DLDDSDKKoin_coreQualifier>)scopeQualifier definitions:(DLDDSDKMutableSet<DLDDSDKKoin_coreBeanDefinition<id> *> *)definitions __attribute__((swift_name("init(scopeQualifier:definitions:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKoin_coreBeanDefinition<id> *)factoryQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier override:(BOOL)override definition:(id _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition __attribute__((swift_name("factory(qualifier:override:definition:)")));
- (DLDDSDKKoin_coreBeanDefinition<id> *)scopedQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier override:(BOOL)override definition:(id _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition __attribute__((swift_name("scoped(qualifier:override:definition:)")));
- (DLDDSDKKoin_coreBeanDefinition<id> *)singleQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier override:(BOOL)override definition:(id _Nullable (^)(DLDDSDKKoin_coreScope *, DLDDSDKKoin_coreDefinitionParameters *))definition __attribute__((swift_name("single(qualifier:override:definition:)"))) __attribute__((unavailable("Can't use Single in a scope. Use Scoped instead")));
@property (readonly) DLDDSDKMutableSet<DLDDSDKKoin_coreBeanDefinition<id> *> *definitions __attribute__((swift_name("definitions")));
@property (readonly) id<DLDDSDKKoin_coreQualifier> scopeQualifier __attribute__((swift_name("scopeQualifier")));
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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreKind")))
@interface DLDDSDKKoin_coreKind : DLDDSDKKotlinEnum<DLDDSDKKoin_coreKind *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKKoin_coreKind *single __attribute__((swift_name("single")));
@property (class, readonly) DLDDSDKKoin_coreKind *factory __attribute__((swift_name("factory")));
+ (DLDDSDKKotlinArray<DLDDSDKKoin_coreKind *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreProperties")))
@interface DLDDSDKKoin_coreProperties : DLDDSDKBase
- (instancetype)initWithData:(DLDDSDKMutableDictionary<NSString *, id> *)data __attribute__((swift_name("init(data:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKoin_coreProperties *)doCopyData:(DLDDSDKMutableDictionary<NSString *, id> *)data __attribute__((swift_name("doCopy(data:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (id _Nullable)getKey:(NSString *)key __attribute__((swift_name("get(key:)")));
- (id _Nullable)getOrNullKey:(NSString *)key __attribute__((swift_name("getOrNull(key:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (void)setKey:(NSString *)key value:(id _Nullable)value __attribute__((swift_name("set(key:value:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreCallbacks")))
@interface DLDDSDKKoin_coreCallbacks<T> : DLDDSDKBase
- (instancetype)initWithOnClose:(void (^ _Nullable)(T _Nullable))onClose __attribute__((swift_name("init(onClose:)"))) __attribute__((objc_designated_initializer));
- (void (^ _Nullable)(T _Nullable))component1 __attribute__((swift_name("component1()")));
- (DLDDSDKKoin_coreCallbacks<T> *)doCopyOnClose:(void (^ _Nullable)(T _Nullable))onClose __attribute__((swift_name("doCopy(onClose:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) void (^ _Nullable onClose)(T _Nullable) __attribute__((swift_name("onClose")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreScopeDefinition")))
@interface DLDDSDKKoin_coreScopeDefinition : DLDDSDKBase
- (instancetype)initWithQualifier:(id<DLDDSDKKoin_coreQualifier>)qualifier isRoot:(BOOL)isRoot __attribute__((swift_name("init(qualifier:isRoot:)"))) __attribute__((objc_designated_initializer));
- (id<DLDDSDKKoin_coreQualifier>)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (DLDDSDKKoin_coreScopeDefinition *)doCopyQualifier:(id<DLDDSDKKoin_coreQualifier>)qualifier isRoot:(BOOL)isRoot __attribute__((swift_name("doCopy(qualifier:isRoot:)")));
- (DLDDSDKKoin_coreBeanDefinition<id> *)declareNewDefinitionInstance:(id _Nullable)instance defQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)defQualifier secondaryTypes:(NSArray<id<DLDDSDKKotlinKClass>> * _Nullable)secondaryTypes override:(BOOL)override __attribute__((swift_name("declareNewDefinition(instance:defQualifier:secondaryTypes:override:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (void)removeBeanDefinition:(DLDDSDKKoin_coreBeanDefinition<id> *)beanDefinition __attribute__((swift_name("remove(beanDefinition:)")));
- (void)saveBeanDefinition:(DLDDSDKKoin_coreBeanDefinition<id> *)beanDefinition forceOverride:(BOOL)forceOverride __attribute__((swift_name("save(beanDefinition:forceOverride:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) DLDDSDKMutableSet<DLDDSDKKoin_coreBeanDefinition<id> *> *definitions __attribute__((swift_name("definitions")));
@property (readonly) BOOL isRoot __attribute__((swift_name("isRoot")));
@property (readonly) id<DLDDSDKKoin_coreQualifier> qualifier __attribute__((swift_name("qualifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreKoin")))
@interface DLDDSDKKoin_coreKoin : DLDDSDKBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (id _Nullable)bindPrimaryType:(id<DLDDSDKKotlinKClass>)primaryType secondaryType:(id<DLDDSDKKotlinKClass>)secondaryType parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("bind(primaryType:secondaryType:parameters:)")));
- (id _Nullable)bindParameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("bind(parameters:)")));
- (void)close __attribute__((swift_name("close()")));
- (DLDDSDKKoin_coreScope *)createScopeT:(id<DLDDSDKKoin_coreKoinScopeComponent>)t __attribute__((swift_name("createScope(t:)")));
- (DLDDSDKKoin_coreScope *)createScopeScopeId:(NSString *)scopeId __attribute__((swift_name("createScope(scopeId:)")));
- (DLDDSDKKoin_coreScope *)createScopeScopeId:(NSString *)scopeId source:(id _Nullable)source __attribute__((swift_name("createScope(scopeId:source:)")));
- (DLDDSDKKoin_coreScope *)createScopeScopeId:(NSString *)scopeId qualifier:(id<DLDDSDKKoin_coreQualifier>)qualifier source:(id _Nullable)source __attribute__((swift_name("createScope(scopeId:qualifier:source:)")));
- (void)declareInstance:(id _Nullable)instance qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier secondaryTypes:(NSArray<id<DLDDSDKKotlinKClass>> *)secondaryTypes override:(BOOL)override __attribute__((swift_name("declare(instance:qualifier:secondaryTypes:override:)")));
- (void)deletePropertyKey:(NSString *)key __attribute__((swift_name("deleteProperty(key:)")));
- (void)deleteScopeScopeId:(NSString *)scopeId __attribute__((swift_name("deleteScope(scopeId:)")));
- (id _Nullable)getClazz:(id<DLDDSDKKotlinKClass>)clazz qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("get(clazz:qualifier:parameters:)")));
- (id)getQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("get(qualifier:parameters:)")));
- (NSArray<id> *)getAll __attribute__((swift_name("getAll()")));
- (DLDDSDKKoin_coreScope *)getOrCreateScopeScopeId:(NSString *)scopeId __attribute__((swift_name("getOrCreateScope(scopeId:)")));
- (DLDDSDKKoin_coreScope *)getOrCreateScopeScopeId:(NSString *)scopeId qualifier:(id<DLDDSDKKoin_coreQualifier>)qualifier source:(id _Nullable)source __attribute__((swift_name("getOrCreateScope(scopeId:qualifier:source:)")));
- (id _Nullable)getOrNullClazz:(id<DLDDSDKKotlinKClass>)clazz qualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("getOrNull(clazz:qualifier:parameters:)")));
- (id _Nullable)getOrNullQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("getOrNull(qualifier:parameters:)")));
- (id _Nullable)getPropertyKey:(NSString *)key __attribute__((swift_name("getProperty(key:)")));
- (id)getPropertyKey:(NSString *)key defaultValue:(id)defaultValue __attribute__((swift_name("getProperty(key:defaultValue:)")));
- (DLDDSDKKoin_coreScope *)getRootScope __attribute__((swift_name("getRootScope()")));
- (DLDDSDKKoin_coreScope *)getScopeScopeId:(NSString *)scopeId __attribute__((swift_name("getScope(scopeId:)")));
- (DLDDSDKKoin_coreScope * _Nullable)getScopeOrNullScopeId:(NSString *)scopeId __attribute__((swift_name("getScopeOrNull(scopeId:)")));
- (id<DLDDSDKKotlinLazy>)injectQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier mode:(DLDDSDKKotlinLazyThreadSafetyMode *)mode parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("inject(qualifier:mode:parameters:)")));
- (id<DLDDSDKKotlinLazy>)injectOrNullQualifier:(id<DLDDSDKKoin_coreQualifier> _Nullable)qualifier mode:(DLDDSDKKotlinLazyThreadSafetyMode *)mode parameters:(DLDDSDKKoin_coreDefinitionParameters *(^ _Nullable)(void))parameters __attribute__((swift_name("injectOrNull(qualifier:mode:parameters:)")));
- (void)loadModulesModules:(NSArray<DLDDSDKKoin_coreModule *> *)modules createEagerInstances:(BOOL)createEagerInstances __attribute__((swift_name("loadModules(modules:createEagerInstances:)")));
- (void)setPropertyKey:(NSString *)key value:(id)value __attribute__((swift_name("setProperty(key:value:)")));
- (void)setupLoggerLogger:(DLDDSDKKoin_coreLogger *)logger __attribute__((swift_name("setupLogger(logger:)")));
- (void)unloadModulesModules:(NSArray<DLDDSDKKoin_coreModule *> *)modules createEagerInstances:(BOOL)createEagerInstances __attribute__((swift_name("unloadModules(modules:createEagerInstances:)")));
@property (readonly) DLDDSDKKoin_coreLogger *logger __attribute__((swift_name("logger")));
@property (readonly) DLDDSDKKoin_corePropertyRegistry *propertyRegistry __attribute__((swift_name("propertyRegistry")));
@property (readonly) DLDDSDKKoin_coreScopeRegistry *scopeRegistry __attribute__((swift_name("scopeRegistry")));
@end;

__attribute__((swift_name("KotlinLazy")))
@protocol DLDDSDKKotlinLazy
@required
- (BOOL)isInitialized __attribute__((swift_name("isInitialized()")));
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinLazyThreadSafetyMode")))
@interface DLDDSDKKotlinLazyThreadSafetyMode : DLDDSDKKotlinEnum<DLDDSDKKotlinLazyThreadSafetyMode *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKKotlinLazyThreadSafetyMode *synchronized __attribute__((swift_name("synchronized")));
@property (class, readonly) DLDDSDKKotlinLazyThreadSafetyMode *publication __attribute__((swift_name("publication")));
@property (class, readonly) DLDDSDKKotlinLazyThreadSafetyMode *none __attribute__((swift_name("none")));
+ (DLDDSDKKotlinArray<DLDDSDKKotlinLazyThreadSafetyMode *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((swift_name("Koin_coreScopeCallback")))
@protocol DLDDSDKKoin_coreScopeCallback
@required
- (void)onScopeCloseScope:(DLDDSDKKoin_coreScope *)scope __attribute__((swift_name("onScopeClose(scope:)")));
@end;

__attribute__((swift_name("Koin_coreLogger")))
@interface DLDDSDKKoin_coreLogger : DLDDSDKBase
- (instancetype)initWithLevel:(DLDDSDKKoin_coreLevel *)level __attribute__((swift_name("init(level:)"))) __attribute__((objc_designated_initializer));
- (void)debugMsg:(NSString *)msg __attribute__((swift_name("debug(msg:)")));
- (void)errorMsg:(NSString *)msg __attribute__((swift_name("error(msg:)")));
- (void)infoMsg:(NSString *)msg __attribute__((swift_name("info(msg:)")));
- (BOOL)isAtLvl:(DLDDSDKKoin_coreLevel *)lvl __attribute__((swift_name("isAt(lvl:)")));
- (void)logLevel:(DLDDSDKKoin_coreLevel *)level msg:(NSString *)msg __attribute__((swift_name("log(level:msg:)")));
@property DLDDSDKKoin_coreLevel *level __attribute__((swift_name("level")));
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

__attribute__((swift_name("Koin_coreKoinComponent")))
@protocol DLDDSDKKoin_coreKoinComponent
@required
- (DLDDSDKKoin_coreKoin *)getKoin __attribute__((swift_name("getKoin()")));
@end;

__attribute__((swift_name("Koin_coreKoinScopeComponent")))
@protocol DLDDSDKKoin_coreKoinScopeComponent <DLDDSDKKoin_coreKoinComponent>
@required
- (void)closeScope __attribute__((swift_name("closeScope()")));
@property (readonly) DLDDSDKKoin_coreScope *scope __attribute__((swift_name("scope")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_corePropertyRegistry")))
@interface DLDDSDKKoin_corePropertyRegistry : DLDDSDKBase
- (instancetype)initWith_koin:(DLDDSDKKoin_coreKoin *)_koin __attribute__((swift_name("init(_koin:)"))) __attribute__((objc_designated_initializer));
- (void)close __attribute__((swift_name("close()")));
- (void)deletePropertyKey:(NSString *)key __attribute__((swift_name("deleteProperty(key:)")));
- (id _Nullable)getPropertyKey:(NSString *)key __attribute__((swift_name("getProperty(key:)")));
- (void)savePropertiesProperties:(NSDictionary<NSString *, id> *)properties __attribute__((swift_name("saveProperties(properties:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreScopeRegistry")))
@interface DLDDSDKKoin_coreScopeRegistry : DLDDSDKBase
- (instancetype)initWith_koin:(DLDDSDKKoin_coreKoin *)_koin __attribute__((swift_name("init(_koin:)"))) __attribute__((objc_designated_initializer));
- (DLDDSDKKoin_coreScope *)createScopeScopeId:(NSString *)scopeId qualifier:(id<DLDDSDKKoin_coreQualifier>)qualifier source:(id _Nullable)source __attribute__((swift_name("createScope(scopeId:qualifier:source:)")));
- (void)declareDefinitionBean:(DLDDSDKKoin_coreBeanDefinition<id> *)bean __attribute__((swift_name("declareDefinition(bean:)")));
- (void)deleteScopeScope:(DLDDSDKKoin_coreScope *)scope __attribute__((swift_name("deleteScope(scope:)")));
- (void)deleteScopeScopeId:(NSString *)scopeId __attribute__((swift_name("deleteScope(scopeId:)")));
- (DLDDSDKKoin_coreScope * _Nullable)getScopeOrNullScopeId:(NSString *)scopeId __attribute__((swift_name("getScopeOrNull(scopeId:)")));
- (int32_t)size __attribute__((swift_name("size()")));
- (void)unloadModulesModules:(id)modules __attribute__((swift_name("unloadModules(modules:)")));
- (void)unloadModulesModule:(DLDDSDKKoin_coreModule *)module __attribute__((swift_name("unloadModules(module:)")));
@property (readonly) DLDDSDKKoin_coreScope *rootScope __attribute__((swift_name("rootScope")));
@property (readonly) NSDictionary<NSString *, DLDDSDKKoin_coreScopeDefinition *> *scopeDefinitions __attribute__((swift_name("scopeDefinitions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Koin_coreLevel")))
@interface DLDDSDKKoin_coreLevel : DLDDSDKKotlinEnum<DLDDSDKKoin_coreLevel *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) DLDDSDKKoin_coreLevel *debug __attribute__((swift_name("debug")));
@property (class, readonly) DLDDSDKKoin_coreLevel *info __attribute__((swift_name("info")));
@property (class, readonly) DLDDSDKKoin_coreLevel *error __attribute__((swift_name("error")));
@property (class, readonly) DLDDSDKKoin_coreLevel *none __attribute__((swift_name("none")));
+ (DLDDSDKKotlinArray<DLDDSDKKoin_coreLevel *> *)values __attribute__((swift_name("values()")));
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
