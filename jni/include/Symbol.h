#ifndef _SYMBOL_H_
#define _SYMBOL_H_

#ifdef __cplusplus
extern "C" {
#endif
    

#include "yoyon_types.h"


//	Fingerprint Security Level
#define		MIN_SECRITY_LEVEL				1
#define 	MAX_SECURITY_LEVEL				5
#define		DEFAULT_SECURITY_LEVEL			3
//	Fingerprint Duplication Check
#define		DUPLICATION_ON				1
#define 	DUPLICATION_OFF				0
#define		DEFAULT_DUPLICATION			DUPLICATION_ON

//	可注册的最多指纹个数
#define	MAX_RECORD_COUNT			(6000)
//模板大小
#define GD_USER_RECORD_SIZE				(498)


////*******************Commands*****************************************/
///***************************************************************************/
#define CMD_INIT_FPLIB_CODE						0x0100
#define CMD_VERIFY_CODE							0x0101
#define CMD_IDENTIFY_CODE						0x0102
#define CMD_ENROLL_CODE							0x0103
#define CMD_ENROLL_ONETIME_CODE					0x0104
#define CMD_CLEAR_TEMPLATE_CODE					0x0105
#define CMD_CLEAR_ALLTEMPLATE_CODE				0x0106
#define CMD_GET_EMPTY_ID_CODE					0x0107
#define CMD_GET_TEMPLATE_STATUS_CODE			0x0108
#define CMD_GET_BROKEN_TEMPLATE_CODE			0x0109
#define CMD_READ_TEMPLATE_CODE					0x010A
#define CMD_WRITE_TEMPLATE_CODE					0x010B
#define CMD_SET_SECURITYLEVEL_CODE				0x010C
#define	CMD_GET_SECURITYLEVEL_CODE				0x010D
#define CMD_GET_FW_VERSION_CODE					0x0112
#define CMD_FINGER_DETECT_CODE					0x0113
#define CMD_SET_DUP_CHECK_CODE					0x0115
#define	CMD_GET_DUP_CHECK_CODE					0x0116
#define CMD_GET_IMAGE_CODE                      0x0125
#define	CMD_GET_ENROLL_COUNT_CODE				0x0126
#define	CMD_SLED_CONTROL_CODE	 				0x0127
#define	CMD_READ_CAM_REG_CODE					0x0128
#define	CMD_WRITE_CAM_REG_CODE					0x0129
#define CMD_SET_AUTO_LEARN_CODE					0x012A
#define CMD_READ_LICENSE_CODE					0x0181		 
#define CMD_READ_SENSOR_PARAM_CODE              0x0190
#define CMD_WRITE_SENSOR_PARAM_CODE             0x0191
#define CMD_ADJUST_BRIGHT_CODE					0x0196
#define CMD_SET_BRIGHT_CODE 					0x0197
#define CMD_GET_FRAME_CODE 					    0x0198


#define CMD_STOP_FPLIB_CODE 					0xaaaa

///*******************************************************************************
//* Error Code
///********************************************************************************/	 

#define	ERR_FAIL					1
#define	ERR_SUCCESS					0
#define	ERR_CONTINUE				2

#define	ERR_INIT_CMOS				0x10
#define	ERR_VERIFY					0x11
#define	ERR_IDENTIFY				0x12
#define	ERR_TMPL_EMPTY				0x13
#define	ERR_TMPL_NOT_EMPTY			0x14
#define	ERR_ALL_TMPL_EMPTY			0x15
#define	ERR_EMPTY_ID_NOEXIST		0x16
#define	ERR_BROKEN_ID_NOEXIST		0x17												    
#define	ERR_INVALID_TMPL_DATA		0x18
#define	ERR_DUPLICATION_ID			0x19
#define	ERR_TOO_FAST				0x20
#define	ERR_BAD_QUALITY				0x21
#define	ERR_SMALL_LINES				0x22
#define	ERR_GENERALIZE				0x30
#define	ERR_INTERNAL				0x50
#define	ERR_MEMORY					0x51
#define	ERR_EXCEPTION				0x52
#define	ERR_INVALID_TMPL_NO			0x60
#define	ERR_INVALID_SEC_VAL			0x61
#define	ERR_INVALID_BAUDRATE		0x63
#define	ERR_DEVICE_ID_EMPTY			0x64
#define	ERR_INVALID_DUP_VAL			0x65
#define	ERR_INVALID_PARAM			0x70
#define	GD_DETECT_FINGER			0x71
#define	GD_NO_DETECT_FINGER			0x72
#define ERR_EMPTY_IMAGE          	0x73
#define ERR_DEVICE_NOT_OPEN         0x74
#define ERR_SYS_DATA          	    0x74
#define ERR_BAD_CHIP          	    0x75

#define	GD_TEMPLATE_NOT_EMPTY		0x01
#define	GD_TEMPLATE_EMPTY			0x00

#define ERR_INCORRECT_COMMAND       0xFF



UINT8	CommandProcess( UINT16 p_wCmdCode, UINT32 p_nParam1, UINT32 p_nParam2, UINT32 p_nParam3 );
    
#ifdef __cplusplus
}
#endif




#endif