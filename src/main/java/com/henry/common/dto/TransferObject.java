package com.henry.common.dto;

import java.io.Serializable;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description:  
 * Tagging interface that any concrete transfer object must implement. Unless
 * otherwise stated any property of a transfer object may accept and return
 * {@code null}; and <i>collections</i> should never be {@code null} but might
 * be empty. At best a transfer object should be immutable.
 **/

public interface TransferObject extends Serializable {
    // Tagging interface
}

